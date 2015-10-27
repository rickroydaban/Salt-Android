package applusvelosi.projects.android.salt.models.capex;

import org.json.JSONObject;

import java.util.HashMap;

import applusvelosi.projects.android.salt.utils.OnlineGateway;

/**
 * Created by Velosi on 10/9/15.
 */
public class CapexLineItem {

    private HashMap<String, Object> mapCapexLineItem;

    public CapexLineItem(JSONObject jsonCapexLineItem, OnlineGateway onlineGateway) throws Exception {
        mapCapexLineItem = new HashMap<String, Object>();
        mapCapexLineItem.putAll(OnlineGateway.toMap(jsonCapexLineItem));
        mapCapexLineItem.put("BaseCurrencyID", Integer.parseInt(mapCapexLineItem.get("BaseCurrencyID").toString()));
        mapCapexLineItem.put("CapexID", Integer.parseInt(mapCapexLineItem.get("CapexID").toString()));
        mapCapexLineItem.put("CapexLineItemID", Integer.parseInt(mapCapexLineItem.get("CapexLineItemID").toString()));
        mapCapexLineItem.put("CategoryID", Integer.parseInt(mapCapexLineItem.get("CategoryID").toString()));
        mapCapexLineItem.put("CurrencyID", Integer.parseInt(mapCapexLineItem.get("CurrencyID").toString()));
        mapCapexLineItem.put("RequesterID", Integer.parseInt(mapCapexLineItem.get("RequesterID").toString()));
        //dates
        mapCapexLineItem.put("DateCreated", onlineGateway.dJsonizeDate(mapCapexLineItem.get("DateCreated").toString()));
    }

    public CapexLineItem(HashMap<String, Object> mapCapexLineItem){
        this.mapCapexLineItem = new HashMap<String, Object>();
        this.mapCapexLineItem.putAll(mapCapexLineItem);
    }

    public HashMap<String, Object> getMap(){
        return mapCapexLineItem;
    }

    public float getAmount() { return Float.parseFloat(mapCapexLineItem.get("Amount").toString()); }

    public float propAmountInUSD(){ return Float.parseFloat(mapCapexLineItem.get("AmountInUSD").toString()); }

    public int getBaseCurrencyID(){ return Integer.parseInt(mapCapexLineItem.get("BaseCurrencyID").toString()); }

    public String getBaseCurrencyName(){ return  mapCapexLineItem.get("BaseCurrencyName").toString(); }

    public String getBaseCurrencyThree(){ return mapCapexLineItem.get("BaseCurrencySymbol").toString(); }

    public float getBaseExchangeRate(){ return Float.parseFloat(mapCapexLineItem.get("BaseExchangeRate").toString()); }

    public int getCapexID(){ return Integer.parseInt(mapCapexLineItem.get("CapexID").toString()); }

    public int getCapexLineItemID(){ return (int)Float.parseFloat(mapCapexLineItem.get("CapexLineItemID").toString()); }

    public String getCapexLineItemNumber(){ return mapCapexLineItem.get("CapexLineItemNumber").toString(); }

    public String getCapexNumber(){ return mapCapexLineItem.get("CapexNumber").toString(); }

    public int getCategoryID(){ return Integer.parseInt(mapCapexLineItem.get("CategoryID").toString()); }

    public String getCategoryName(){ return mapCapexLineItem.get("CategoryName").toString(); }

    public int getCurrencyID(){ return Integer.parseInt(mapCapexLineItem.get("CurrencyID").toString()); }

    public String getCurrencyName(){ return mapCapexLineItem.get("CurrencyName").toString(); }

    public String getCurrencyThree(){ return mapCapexLineItem.get("CurrencySymbol").toString(); }

    public String getDateCreated(){ return mapCapexLineItem.get("DateCreated").toString(); }

    public String getDesc(){ return mapCapexLineItem.get("Description").toString(); }

    public float getQuantity(){ return Float.parseFloat(mapCapexLineItem.get("Quantity").toString()); }

    public int getRequesterID(){ return Integer.parseInt(mapCapexLineItem.get("RequesterID").toString()); }

    public String getRequesterName(){ return mapCapexLineItem.get("RequesterName").toString(); }

    public float getUnitCost(){ return Float.parseFloat(mapCapexLineItem.get("UnitCost").toString()); }

    public float getUSDExchangeRate(){ return Float.parseFloat(mapCapexLineItem.get("USDExchangeRate").toString()); }

    public float getUsefulLife(){ return Float.parseFloat(mapCapexLineItem.get("UsefulLife").toString()); }

}