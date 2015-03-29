package fi.oma.peli;


import fi.oma.database.DataActivity;
import fi.oma.tasopeli.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	
	public void startActivity(View view) {
		 Intent intent = new Intent(MainActivity.this,DataActivity.class);
         startActivity(intent);
	}
	public void startOptions(View view) {
		 Intent intent = new Intent(MainActivity.this,OptionsActivity.class);
        startActivity(intent);
	}
	
}

