package com.androidapp.trendingrepoclient.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidapp.trendingrepoclient.R
import com.androidapp.trendingrepoclient.data.db.GithubDb
import com.androidapp.trendingrepoclient.data.db.GithubDbProvider
import com.androidapp.trendingrepoclient.entity.Token
import com.androidapp.trendingrepoclient.rx.RxBus
import com.androidapp.trendingrepoclient.ui.event.RepositoryClickEvent
import com.androidapp.trendingrepoclient.ui.repositoriesList.RepositoryListFragment
import com.androidapp.trendingrepoclient.ui.repositorydetail.RepositoryDetailFragment
import com.androidapp.trendingrepoclient.ui.util.addFragment
import com.androidapp.trendingrepoclient.ui.util.gone
import com.androidapp.trendingrepoclient.ui.util.replaceFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.mtramin.rxfingerprint.EncryptionMethod
import com.mtramin.rxfingerprint.RxFingerprint
import com.mtramin.rxfingerprint.data.FingerprintAuthenticationResult
import com.mtramin.rxfingerprint.data.FingerprintDecryptionResult
import com.mtramin.rxfingerprint.data.FingerprintEncryptionResult
import com.mtramin.rxfingerprint.data.FingerprintResult
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class LoginActivity : AppCompatActivity() {

    private lateinit var subscribeRepositorySelectedEvent: Disposable
    private lateinit var provider: OAuthProvider.Builder
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var fingerPrintDisposable: Disposable
    private val ioExecutor: Executor = Executors.newSingleThreadExecutor()
    private lateinit var githubDb: GithubDb

    private var encryptedToken: String? = null
    private var decryptedToken: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        githubDb = GithubDbProvider.getInstance(this.applicationContext)

        firebaseAuth = FirebaseAuth.getInstance()
        provider = OAuthProvider.newBuilder("github.com")
        val scopes: ArrayList<String?> =
            object : ArrayList<String?>() {
                init {
                    add("user:email")
                }
            }
        provider.setScopes(scopes)

        subscribeObservable()
        initListener()

        getTokenFromDb()

    }

    private fun initListener() {
        btn_login.setOnClickListener {
            if (checkForBiometrics() && decryptedToken != null) {
                authWithBiometrics()
            } else {
                authWithGithubLogin()
            }
        }
    }

    private fun authWithBiometrics() {
        fingerPrintDisposable = RxFingerprint.authenticate(this)
            .subscribe(
                { fingerprintAuthenticationResult: FingerprintAuthenticationResult ->
                    when (fingerprintAuthenticationResult.result) {
                        FingerprintResult.FAILED -> {
                            Toast.makeText(
                                applicationContext,
                                "Fingerprint not recognized, try again! ", Toast.LENGTH_SHORT
                            )
                                .show()
                            authWithGithubLogin()
                        }
                        FingerprintResult.HELP -> {
                            Toast.makeText(
                                applicationContext, "${fingerprintAuthenticationResult.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        FingerprintResult.AUTHENTICATED -> {
                            fingerprintAuthenticationResult.result.name
                            Log.d(
                                "fingerprintAuthenticationResult",
                                ".result:${fingerprintAuthenticationResult.result.ordinal} "
                            )
                            Toast.makeText(
                                applicationContext,
                                "Authentication succeeded!", Toast.LENGTH_SHORT
                            )
                                .show()

                            if (decryptedToken != null) {
                                startLoginFlow(decryptedToken!!)

                            }


                        }
                    }
                }
            ) { throwable: Throwable? ->
                Log.e(
                    "ERROR",
                    "authenticate",
                    throwable
                )
            }
    }

    private fun encryptWithBiometrics(keyName: String, stringToEncrypt: String) {
        fingerPrintDisposable =
            RxFingerprint.encrypt(EncryptionMethod.AES, this, keyName, stringToEncrypt)
                .subscribe(
                    { encryptionResult: FingerprintEncryptionResult ->
                        when (encryptionResult.result) {
                            FingerprintResult.FAILED -> Log.e(
                                "encryptionResult",
                                "encryptionResult.result:FAILED "
                            )
                            FingerprintResult.HELP -> Log.d(
                                "encryptionResult",
                                ".result:${encryptionResult.message} "
                            )
                            FingerprintResult.AUTHENTICATED -> {
                                insertTokenToDb(encryptionResult.encrypted)
                                Log.d(
                                    "encryptionResult",
                                    "Successfully encrypted! ${encryptionResult.encrypted} "
                                )
                            }
                        }
                    }
                ) { throwable: Throwable ->
                    Log.e("ERROR", "encrypt", throwable)
                }

    }

    private fun insertTokenToDb(token: String) {
        deleteAllTokenFromDb()
        ioExecutor.execute {
            githubDb.runInTransaction {
                githubDb.token().insert(Token(token))
//                Log.d("serife", "insertTokenToDb: $token")
            }
        }
    }

    private fun deleteAllTokenFromDb() {
        ioExecutor.execute {
            githubDb.runInTransaction {
                githubDb.token().deleteAll()
//                Log.d("serife", "deleteAllTokenFromDb")
            }
        }
    }


    private fun getTokenFromDb() {

        ioExecutor.execute {
            githubDb.runInTransaction {
                val tokenList = githubDb.token().getToken()
                if (tokenList != null && tokenList.isNotEmpty()) {
                    encryptedToken = tokenList[0].id
//                    Log.d("serife", "encryptedToken from db: $encryptedToken")
                    decryptWithBiometrics("accessToken",encryptedToken!!)
                }
            }
        }

    }


    private fun decryptWithBiometrics(keyName: String, encryptedValue: String) {
        fingerPrintDisposable =
            RxFingerprint.decrypt(EncryptionMethod.AES, this, keyName, encryptedValue)
                .subscribe(
                    { decryptionResult: FingerprintDecryptionResult ->
                        when (decryptionResult.result) {
                            FingerprintResult.FAILED -> Log.e(
                                "decryptionResult",
                                "decryptionResult.result:FAILED "
                            )
                            FingerprintResult.HELP -> Log.d(
                                "decryptionResult",
                                ".result:${decryptionResult.message} "
                            )
                            FingerprintResult.AUTHENTICATED -> {
                                decryptedToken = decryptionResult.decrypted

//                                Log.d(
//                                    "decryptionResult",
//                                    "Successfully decrypted! ${decryptionResult.decrypted}"
//
//                                )
                            }
                        }
                    }
                ) { throwable: Throwable ->
                    //noinspection StatementWithEmptyBody
                    if (RxFingerprint.keyInvalidated(throwable)) {
                        // The keys you wanted to use are invalidated because the user has turned off his
                        // secure lock screen or changed the fingerprints stored on the device
                        // You have to re-encrypt the data to access it
                    }
                    Log.e("ERROR", "decrypt", throwable)
                }

    }

    private fun authWithGithubLogin() {
        val pendingResultTask: Task<AuthResult>? = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) { // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener {
                    val token = it?.credential as OAuthCredential
                    val accessToken = token.accessToken
                    startLoginFlow(accessToken)
                }
                .addOnFailureListener {
                    // Handle failure.
                }
        } else { // There's no pending result so you need to start the sign-in flow.
            startSignInFlow()
        }
    }

    private fun startLoginFlow(accessToken: String) {
        btn_login.gone()
        encryptWithBiometrics("accessToken", accessToken)

        addFragment(RepositoryListFragment.newInstance(accessToken), R.id.frame_main_content, true)
    }

    private fun checkForBiometrics(): Boolean {
        return RxFingerprint.isAvailable(this)
    }

    private fun startSignInFlow() {
        firebaseAuth
            .startActivityForSignInWithProvider( /* activity= */this, provider.build())
            .addOnSuccessListener {

                val token = it?.credential as OAuthCredential
                val accessToken = token.accessToken
                startLoginFlow(accessToken)
            }
            .addOnFailureListener {
                // Handle failure.

            }
    }

    private fun subscribeObservable() {

        subscribeRepositorySelectedEvent =
            RxBus.listen(RepositoryClickEvent::class.java).subscribe {

                replaceFragment(RepositoryDetailFragment.newInstance(it.repository), R.id.frame_main_content, true)

            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun unSubscribeObservable() {
        if (!subscribeRepositorySelectedEvent.isDisposed) {
            subscribeRepositorySelectedEvent.dispose()
        }

        if (fingerPrintDisposable.isDisposed) {
            fingerPrintDisposable.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unSubscribeObservable()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }
}
