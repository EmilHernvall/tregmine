package info.tregmine.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.avaje.ebeaninternal.server.lib.util.GeneralException;

public class ListStore {
	
	private File storageFile;
	private ArrayList<String> values;
	
	public ListStore(File file){
		this.storageFile = file;
		this.values = new ArrayList<String>();
		if(!this.storageFile.exists()){
			try{
				this.storageFile.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public void load(){
		try{
			DataInputStream input = new DataInputStream(new FileInputStream(this.storageFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;
			while((line = reader.readLine()) != null){
				if(!this.contains(line)){
					this.values.add(line);
				}
			}
			reader.close();
			input.close();
		}catch(GeneralException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void save(){
		try {
			FileWriter stream = new FileWriter(this.storageFile);
			BufferedWriter writer = new BufferedWriter(stream);
			for(String line : this.values){
				writer.write(line);
				writer.newLine();
			}
			writer.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean contains(String value){
		return this.values.contains(value);
	}
	public void add(String v){
		if(!this.contains(v)){
		this.values.add(v);
		}
	}
	public void remove(String v){
		this.values.remove(v);
	}
	public ArrayList<String> getValues(){
		return this.values;
	}
}
