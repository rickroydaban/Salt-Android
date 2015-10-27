package applusvelosi.projects.android.salt.models.capex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

/**
 * Created by Velosi on 10/9/15.
 */
public class CapexLineItemQoutation {
    private HashMap<String, Object> mapCapexLineItemQoutation;
    private ArrayList<HashMap<String, Object>> mapDocuments;

    public CapexLineItemQoutation(JSONObject jsonCapexLineItemQoutation, OnlineGateway onlineGateway) throws Exception{
        mapCapexLineItemQoutation = new HashMap<String, Object>();
        mapCapexLineItemQoutation.putAll(OnlineGateway.toMap(jsonCapexLineItemQoutation));
        mapCapexLineItemQoutation.put("CapexLineItemID", Integer.parseInt(mapCapexLineItemQoutation.get("CapexLineItemID").toString()));
        mapCapexLineItemQoutation.put("CapexLineItemQuotationID", Integer.parseInt(mapCapexLineItemQoutation.get("CapexLineItemQuotationID").toString()));
        mapCapexLineItemQoutation.put("CurrencyID", Integer.parseInt(mapCapexLineItemQoutation.get("CurrencyID").toString()));
        mapCapexLineItemQoutation.put("FinancingShemeID", Integer.parseInt(mapCapexLineItemQoutation.get("FinancingSchemeID").toString()));

        mapDocuments = (ArrayList<HashMap<String, Object>>)OnlineGateway.toList(jsonCapexLineItemQoutation.getJSONArray("Documents"));
    }

    public CapexLineItemQoutation(HashMap<String, Object> mapQoutation, SaltApplication app){
        this.mapCapexLineItemQoutation = new HashMap<String, Object>();
        this.mapCapexLineItemQoutation.putAll(mapQoutation);

        mapDocuments = app.gson.fromJson(mapCapexLineItemQoutation.get("Documents").toString(), app.types.arrayListOfHashmapOfStringObject);
    }

    public HashMap<String, Object> getMap(){
        return mapCapexLineItemQoutation;
    }

    public float getAmount(){ return Float.parseFloat(mapCapexLineItemQoutation.get("Amount").toString()); }

    public float getAmountInUSD(){ return Float.parseFloat(mapCapexLineItemQoutation.get("AmountInUSD").toString()); }

    public int getCapexLineItemID(){ return Integer.parseInt(mapCapexLineItemQoutation.get("CapexLineItemID").toString()); }

    public int getCapexLineItemQoutationID(){ return Integer.parseInt(mapCapexLineItemQoutation.get("CapexLineItemQuotationID").toString()); }

    public int getCurrencyID(){ return Integer.parseInt(mapCapexLineItemQoutation.get("CurrencyID").toString()); }

    public String getCurrencyThree(){ return mapCapexLineItemQoutation.get("CurrencyName").toString(); }

    public ArrayList<HashMap<String, Object>> getAttachments(){ return mapDocuments; }

    public float getExchangeRate(){ return Float.parseFloat(mapCapexLineItemQoutation.get("ExchangeRate").toString()); }

    public int getFinancingSchemeID(){ return Integer.parseInt(mapCapexLineItemQoutation.get("FinancingSchemeID").toString()); }

    public String getFinancingSchemeName(){ return mapCapexLineItemQoutation.get("FinancingSchemeName").toString();  }

    public boolean isPrimary(){ return Boolean.parseBoolean(mapCapexLineItemQoutation.get("IsPrimary").toString()); }

    public  String getNotes(){ return mapCapexLineItemQoutation.get("Note").toString(); }

    public String getPaymentTerm(){ return mapCapexLineItemQoutation.get("PaymentTerm").toString(); }

    public String getSupplierName(){ return mapCapexLineItemQoutation.get("SupplierName").toString(); }

}
