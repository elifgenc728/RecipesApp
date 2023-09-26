package com.elifgenc.firebaseprojesi.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elifgenc.firebaseprojesi.R
import com.google.firebase.Timestamp.now
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import java.util.*


class FotografPaylasmaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var  storage: FirebaseStorage

    var secilenGorsel: Uri? =null
    var secilenBitmap: Bitmap? =null

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
         storage = FirebaseStorage.getInstance()
    }

    fun paylas(view: View){
        //UUID -> Universal Unique id(herkese rastgele farklı id veriliyor)
        //depo işlemleri

        val uuid = UUID.randomUUID()
        val gorselIsmi = "${uuid}.jpeg"
        val reference = storage.reference
        val gorselReference = reference.child("images").child(gorselIsmi)
        if (secilenGorsel != null){
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot ->
                val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    //VERİ KAYDETME(Database)
                    //veritabanı işlemleri(burada sadece database' e bağlamayı yaptık,ekranda göstermelik bir şey yapmadık)
                    val guncelKullaniciEmaili = auth.currentUser!!.email.toString()
                    val kullaniciYorumu = yorumText.text.toString()
                    //Timestamp Firebase' in kütüphanesidir.Güncel olan zamanı verir.
                    val tarih = now()

                    //hashmap' te String değerlerimizi yazıyoruz ama gelecek değerlere
                    //any diyoruz çünkü herhangi bir türde olabilir.
                    //yukarıdaki bütün değişkenlerimizi aşağıda database' de hash yöntemi ile oluşturuyoruz.
                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("gorselurl", downloadUrl )
                    postHashMap.put("kullaniciEmail",guncelKullaniciEmaili)
                    postHashMap.put("kullaniciYorumu",kullaniciYorumu)
                    postHashMap.put("tarih", tarih)


                    //aşağıda database' e collection' umuzu oluşturup, yukarıda oluşturduğumuz
                    //HashMap' imizi ekliyoruz ve en sonunda da,
                    //completeListener ile başarılı olup olmama durumunu sorgu yazarak kontrol ediyoruz.
                    database.collection("Post").add(postHashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            //intent yapmaya gerek yok. Çünkü HaberlerActiviy' den buraya gelirken,
                            // finish yapmamıştık. Burada yapabiliriz ve artık uygulamada geri dönebilmeyi de sağlamış oluruz.
                            finish()
                        }

                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()

                }
                }
            }

        }




    fun gorselSec(view:View){
        //güvenlik kontroleri
//aşağıdaki ilk kısım izni kontrol ediyor ve bu eşit değilse permission_granted' e yani izin verildiye eşit değilse demek.
       if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
      //izni almamışız.
           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
           }

       //eğer iznimiz varsa galariye gideceğiz.Zaten izin var
        else{
           val galeriIntent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
           //Aşağıda bir sonuç için bu aktiviteyi başlatacağımızı söylüyouz.
           startActivityForResult(galeriIntent,2)
        }

    }
//istediğimiz izinlerin sonunda ne olacak
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
    if (requestCode==1){
        if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
   //izin verilince yaplacaklar.
            val galeriIntent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //Aşağıda bir sonuç için bu aktiviteyi başlatacağımızı söylüyouz.
            startActivityForResult(galeriIntent,2)
        }
    }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
      secilenGorsel= data.data

            if (secilenGorsel != null){

                if (Build.VERSION.SDK_INT >= 28){
           val source =ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
            secilenBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(secilenBitmap)
            } else{
                secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                imageView.setImageBitmap(secilenBitmap)
            }





        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    }
}