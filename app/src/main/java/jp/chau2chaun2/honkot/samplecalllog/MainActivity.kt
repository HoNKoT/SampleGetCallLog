package jp.chau2chaun2.honkot.samplecalllog

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickedReadCallLog(view: View) {
        if (checkPermission()) {
            listCallLog()
        }
    }

    private fun listCallLog() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val order = CallLog.Calls.DEFAULT_SORT_ORDER
        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            order
        )

        if (cursor?.moveToFirst() == true) {
            do {
                val number: String =
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) ?: ""
                val cachedName: String =
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) ?: ""
                val type: String =
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)) ?: ""
                val date = Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)))
                Log.d("test", "DATE : $date")
                Log.d("test", "NUMBER : $number")
                Log.d("test", "CACHED_NAME : $cachedName")
                Log.d("test", "TYPE : $type")
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    private fun checkPermission(): Boolean {
        // 選択された画像の情報を取得（ストレージされたファイルは要READ_EXTERNAL_STORAGE）
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // 許可されていない
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CALL_LOG)
            ) {
                // すでに１度パーミッションのリクエストが行われていて、
                // ユーザーに「許可しない（二度と表示しないは非チェック）」をされていると
                // この処理が呼ばれます。
                Toast.makeText(this, "Call log permission is off.", Toast.LENGTH_SHORT).show()
                return false

            } else {
                // パーミッションのリクエストを表示
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CALL_LOG),
                    REQUEST_CODE_READ_CALL_LOG
                )
                return false
            }
        }

        return true
    }

    companion object {
        private const val REQUEST_CODE_READ_CALL_LOG = 1
    }
}
