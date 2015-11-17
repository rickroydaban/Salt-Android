package applusvelosi.projects.android.salt.models.claimitems;

import java.io.Serializable;

/**
 * Created by Velosi on 11/16/15.
 */
public class Project implements Serializable{

    private int id;
    private String name;

    public Project(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getID(){ return id; }
    public String getName(){ return name; }
}
