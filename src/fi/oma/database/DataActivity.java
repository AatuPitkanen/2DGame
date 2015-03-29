package fi.oma.database;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import fi.oma.peli.MainActivity;
import fi.oma.peli.OptionsActivity;
import fi.oma.peli.GameActivity;
import fi.oma.tasopeli.R;

public class DataActivity extends Activity implements OnClickListener
{
	EditText editName, editName2;
	Button btnAdd, btnViewAll;
	SQLiteDatabase db;
	
    
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
      
        editName=(EditText)findViewById(R.id.editText1);
        editName2=(EditText)findViewById(R.id.editText2);
        btnAdd=(Button)findViewById(R.id.button1);
        btnViewAll=(Button)findViewById(R.id.ScoreButton);
        btnAdd.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
         
         //CREATING DATABASE
        db=openOrCreateDatabase("ScoreDB", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS score(ename VARCHAR, sname VARCHAR);");
        
		
    } //ADD BUTTON 
    public void onClick(View view)
    {
    	if(view==btnAdd)
    	{
    		if(editName.getText().toString().trim().length()==0||
    				editName2.getText().toString().trim().length()==0)
    		{
    			  showMessage("Error", "Please enter all values");
                  return;
    		}
    		 db.execSQL("INSERT INTO score VALUES('"+editName.getText()+"','"+editName2.getText()+ "');");
    		showMessage("Success", "Record added");
    		clearText();
    	}
    	
    	if(view==btnViewAll)
    	{
    		Cursor c=db.rawQuery("SELECT * FROM score", null);
    		if(c.getCount()==0)
    		{
    			showMessage("Error", "No Highscores yet");
    			return;
    		}
    		StringBuffer buffer=new StringBuffer();
    		while(c.moveToNext())
    		{
    			buffer.append("Etunimi: "+c.getString(0)+" --- ");
    			buffer.append("Sukunimi: "+c.getString(1)+"\n");
    			
    		}
    		showMessage("HighScores", buffer.toString());
    	}
    	
    }
    public void showMessage(String title,String message)
    {
    	Builder builder=new Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.show();
	}
    public void clearText()
    {
    
    	editName.setText("");
    	editName2.setText("");
    	
    }
    public void NewGameClicked(View view) {
		 Intent intent = new Intent(DataActivity.this,GameActivity.class);
       startActivity(intent);
	}
    public void BackClicked(View view) {
		 Intent intent = new Intent(DataActivity.this,MainActivity.class);
      startActivity(intent);
	}
}
