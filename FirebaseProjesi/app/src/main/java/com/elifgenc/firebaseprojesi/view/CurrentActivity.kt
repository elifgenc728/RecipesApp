package com.elifgenc.firebaseprojesi.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elifgenc.firebaseprojesi.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_current.*

class CurrentActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current)

        //yukarıda tanımladığımız auth' a burada değerini veriyoruz.
        auth = FirebaseAuth.getInstance()
     //aşağıdada oluşturmuştuk guncelKullanici değişkenini burada da ,
    //kullanıcı ugulamayı her açtığnda yeniden giriş yapmasına gerek kalmayacak.
        val guncelKullanici = auth.currentUser //currentUser' da yani uygulamaya önceden zaten giriş yapmış kullanıcıyı alıyoruz.
        if (guncelKullanici != null){
            val intent = Intent(this, HaberlerActivity::class.java)
            Toast.makeText(this,"Hoşgeldin ${guncelKullanici}",Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        }

    }
 fun girisYap(view: View){
 auth.signInWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnCompleteListener { task ->
     if (task.isSuccessful){
 //guncelKullanici değişkenini giriş yapıldıgında ekrana gelecek bildirim için oluşturduk.
         val guncelKullanici = auth.currentUser?.email.toString()
         Toast.makeText(this,"Hoşgeldin ${guncelKullanici}",Toast.LENGTH_LONG).show()

         val intent = Intent(this, HaberlerActivity::class.java)
         startActivity(intent)
         finish()
     }
 }.addOnFailureListener { exception ->
     Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
 }
 }
    fun kayitOl(view: View){
        val email= emailText.text.toString()
        val sifre = passwordText.text.toString()

        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener { task ->
            //asenkron çalışıyor
            if(task.isSuccessful){
                //task yani olay başarılı olursa aktiviteye gidelim.
                //aktiviteye gitmeyi intent ile yapabiliriz.
                val intent = Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish() //Komple MainActivity' i kapatalım ki geri dönüş olmasın. Logout fonksiyonu ile geri dönme işlemlerini sonradan ayarlayabiliriz.
            }
            //hata olursa aşağıdaki listener dinleniyor,
            //exception ile de hata mesajını çağırıyoruz.
            //localizedMessage: kullanıcının anlayacagı dilden bir mesaj demek.
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}