package com.vis.kotlingpayintegration

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
        private var uri: Uri? = null
        private var payerName: String? = null
        private var UpiId: String? = null
        private var msgNote: String? = null
        private var sendAmount: String? = null
        private var status: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            pay_layout.setOnClickListener {
                //static values
                payerName = "abc"
                UpiId = "abc@okhdfcbank"
                msgNote = "buying product"
                sendAmount = "1000"

                //if dynamic use this condition
                // if(!payerName.equals("") && !upiId.equals("") && !msgNote.equals("") && !sendAmount.equals("")){
                uri = getUpiPaymentUri(payerName, UpiId, msgNote, sendAmount)
                payWithGpay(GPAY_PACKAGE_NAME)

                // }
                // else {
                //     Toast.makeText(MainActivity.this,"Fill all above details and try again.", Toast.LENGTH_SHORT).show();


                // }
            }
        }

        private fun payWithGpay(packageName: String) {
            if (isAppInstalled(this, packageName)) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(uri)
                intent.setPackage(packageName)
                startActivityForResult(intent, 0)
            } else {
                Toast.makeText(this, "Google pay is not installed. Please install and try again.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (data != null) {
                status = data.getStringExtra("Status").toLowerCase()
            }
            if (RESULT_OK === resultCode && status == "success") {
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show()
                msg!!.setText("Transaction successful of ₹$sendAmount")
                msg!!.setTextColor(Color.GREEN)
            } else {
                Toast.makeText(this, "Transaction cancelled or failed please try again.", Toast.LENGTH_SHORT).show()
                msg!!.setText("Transaction Failed of ₹$sendAmount")
                msg!!.setTextColor(Color.RED)
            }
        }

        companion object {
            const val GPAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
            var payerName: String? = null
            var UpiId: String? = null
            var msgNote: String? = null
            var sendAmount: String? = null
            var status: String? = null
            private fun getUpiPaymentUri(name: String?, upiId: String?, note: String?, amount: String?): Uri {
                return Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", upiId)
                        .appendQueryParameter("pn", name)
                        .appendQueryParameter("tn", note)
                        .appendQueryParameter("am", amount)
                        .appendQueryParameter("cu", "INR")
                        .build()
            }

            fun isAppInstalled(context: Context, packageName: String?): Boolean {
                return try {
                    context.getPackageManager().getApplicationInfo(packageName, 0)
                    true
                } catch (e: PackageManager.NameNotFoundException) {
                    false
                }
            }
        }



    }
