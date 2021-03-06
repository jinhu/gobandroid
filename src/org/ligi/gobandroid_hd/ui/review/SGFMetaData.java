package org.ligi.gobandroid_hd.ui.review;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.json.JSONException;
import org.json.JSONObject;
import org.ligi.android.common.files.FileHelper;
import org.ligi.gobandroid_hd.GobandroidApp;
import org.ligi.tracedroid.logging.Log;

/**
 * stores and gives access to metatata to an SGF file
 * 
 * @author ligi
 * 
 */
public class SGFMetaData {

	public Integer rating = null;
	public boolean is_solved = false;

	private String meta_fname = null;
	private boolean has_data = false;

	public final static String FNAME_ENDING = ".sgfmeta";

	public SGFMetaData(String fname) {
		if (!fname.endsWith(FNAME_ENDING))
			fname += FNAME_ENDING;

		meta_fname = fname;
		try {
			Log.i("got json file "
					+ FileHelper.file2String(new File(meta_fname)));
			JSONObject jObject = new JSONObject(
					FileHelper.file2String(new File(meta_fname)));

			try {
				rating = (Integer) jObject.getInt("rating");
			} catch (org.json.JSONException jse) {
			} // don't care if not there

			try {
				is_solved = (Boolean) jObject.getBoolean("is_solved");
			} catch (org.json.JSONException jse) {
			} // don't care if not there

			has_data = true;
		} catch (Exception e) {
			Log.i("got json file " + e);
		}
	}

	public SGFMetaData(GobandroidApp app) {
		this(app.getGame().getMetaData().getFileName() + FNAME_ENDING);
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getRating() {
		return rating;
	}

	public boolean getIsSolved() {
		return is_solved;
	}

	public boolean hasData() {
		return has_data;
	}

	public void persist() {
		try {
			JSONObject object = new JSONObject();
			try {
				if (rating != null)
					object.put("rating", rating);
				object.put("is_solved", is_solved);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			String json_str = object.toString();

			FileWriter sgf_writer = new FileWriter(new File(meta_fname));

			BufferedWriter out = new BufferedWriter(sgf_writer);

			out.write(json_str);
			out.close();
			sgf_writer.close();
		} catch (Exception e) {
			Log.w("problem writing metadata" + e);
		}

	}

	public void setIsSolved(boolean b) {
		is_solved = b;
	}
}
