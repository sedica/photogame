package es.sedica.photogame.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import es.sedica.photogame.R;
import es.sedica.photogame.components.GameManager;

public class ChoosePhotoActivity extends ActionBarActivity implements
		OnClickListener {

	private static final int CHOOSE_PHOTO_RESULT = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_photo);

		Button btnChoose = (Button) findViewById(R.id.btnChoosePhoto);
		btnChoose.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnChoosePhoto:
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, CHOOSE_PHOTO_RESULT);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		Log.d("ChoosePhotoActivity", "Chosen photo");

		switch (requestCode) {
		case CHOOSE_PHOTO_RESULT:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				Intent intent = new Intent(this, BoardActivity.class);
				try {
					intent.putExtra("initImage", saveTmpImage(selectedImage));
					GameManager gm=new GameManager(3, 4);
					intent.putExtra("game", gm);
					startActivity(intent);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

	private String saveTmpImage(Uri selectedImage) throws IOException {
		InputStream imageStream;
		Bitmap yourSelectedImage = null;
		imageStream = getContentResolver().openInputStream(selectedImage);
		File outputFile = File.createTempFile( this.hashCode()+"",null, this.getCacheDir());
		FileOutputStream fos=new FileOutputStream(outputFile);
		byte[] buffer = new byte[1024];
		int len = imageStream.read(buffer);
		while (len != -1) {
		    fos.write(buffer, 0, len);
		    len = imageStream.read(buffer);
		}
		fos.close();
		return outputFile.getAbsolutePath();
	}

}
