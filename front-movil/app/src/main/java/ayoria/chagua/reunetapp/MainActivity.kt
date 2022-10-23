package ayoria.chagua.reunetapp

import android.os.Bundle
import ayoria.chagua.reunetapp.databinding.ActivityMainBinding

class MainActivity :  BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}