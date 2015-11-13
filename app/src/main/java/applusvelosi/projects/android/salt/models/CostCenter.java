package applusvelosi.projects.android.salt.models;

/**
 * Created by Velosi on 11/6/15.
 */
public class CostCenter {
    private int costCenterID;
    private String costCenterName;

    public CostCenter(int costCenterID, String costCenterName){
        this.costCenterID = costCenterID;
        this.costCenterName = costCenterName;
    }

    public int getCostCenterID(){ return costCenterID; }
    public String getCostCenterName(){ return costCenterName; }

}
