package fi.oma.peli;

import fi.oma.tasopeli.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class OptionsActivity extends Activity {
	
	private SeekBar volumeControl = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		volumeControl = (SeekBar) findViewById(R.id.seekBar1);
		
		volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int progressChanged = 0;

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				progressChanged = progress;
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				Toast.makeText(OptionsActivity.this,"Volume changed tos:"+progressChanged, 
						Toast.LENGTH_SHORT).show();
			}
		});
		
		
		
	}
	
	public void backButtonClicked(View view) {
		finish();
	}
	
}