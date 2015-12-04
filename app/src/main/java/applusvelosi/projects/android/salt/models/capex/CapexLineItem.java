package applusvelosi.projects.android.salt.models.capex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.utils.OnlineGateway;

/**
 * Created by Velosi on 10/9/15.
 */
public class CapexLineItem {

    private boolean isActive;
    private float amount, amountInUSD;
    private int baseCurrencyID;
    private String baseCurrencyName, baseCurrencySymbol;
    private float baseExchangeRate;
    private int capexID, capexLineItemID;
    private String capexLineItemNumber;
    private ArrayList<CapexLineItemQoutation> qoutations;
    private String capexNumber;
    private int categoryID;
    private String categoryName;
    private int currencyID;
    private String currencyName, currencySymbol, dateCreated, desc;
    private int numberOfQoutes, requesterID;
    private float quantity;
    private String requesterName;
    private float unitCost;
    private String uom;
    private float usdExchangeRate, usefulLife;

    public CapexLineItem(JSONObject jsonCapexLineItem) throws Exception {
        isActive = jsonCapexLineItem.getBoolean("Active");
        amount = (float)jsonCapexLineItem.getDouble("Amount");
        amountInUSD = (float)jsonCapexLineItem.getDouble("AmountInUSD");
        baseCurrencyID = jsonCapexLineItem.getInt("BaseCurrencyID");
        baseCurrencyName = jsonCapexLineItem.getString("BaseCurrencyName");
        baseCurrencySymbol = jsonCapexLineItem.getString("BaseCurrencySymbol");
        baseExchangeRate = (float)jsonCapexLineItem.getDouble("BaseExchangeRate");
        capexID = jsonCapexLineItem.getInt("CapexID");
        capexLineItemID = jsonCapexLineItem.getInt("CapexLineItemID");
        capexLineItemNumber = jsonCapexLineItem.getString("CapexLineItemNumber");
        capexNumber = jsonCapexLineItem.getString("CapexNumber");
        categoryID = jsonCapexLineItem.getInt("CategoryID");
        categoryName = jsonCapexLineItem.getString("CategoryName");
        currencyID = jsonCapexLineItem.getInt("CurrencyID");
        currencyName = jsonCapexLineItem.getString("CurrencyName");
        currencySymbol = jsonCapexLineItem.getString("CurrencySymbol");
        dateCreated = jsonCapexLineItem.getString("DateCreated");
        desc = jsonCapexLineItem.getString("Description");
        numberOfQoutes = jsonCapexLineItem.getInt("NumberOfQuotes");
        quantity = (float)jsonCapexLineItem.getDouble("Quantity");
        requesterID = jsonCapexLineItem.getInt("RequesterID");
        requesterName = jsonCapexLineItem.getString("RequesterName");
        unitCost = (float)jsonCapexLineItem.getDouble("UnitCost");
        uom = jsonCapexLineItem.getString("UOM");
        usdExchangeRate = (float)jsonCapexLineItem.getDouble("USDExchangeRate");
        usefulLife = (float)jsonCapexLineItem.getDouble("UsefulLife");

        JSONArray jsonQoutations = jsonCapexLineItem.getJSONArray("CapexLineItemQuotation");
        qoutations = new ArrayList<CapexLineItemQoutation>();
        for(int i=0; i<jsonQoutations.length(); i++)
            qoutations.add(new CapexLineItemQoutation(jsonQoutations.getJSONObject(i)));

    }

    public JSONObject getJSONObject() throws Exception{
        JSONObject jo = new JSONObject();
        jo.put("Active", isActive);
        jo.put("Amount", amount);
        jo.put("AmountInUSD", amountInUSD);
        jo.put("BaseCurrencyID", baseCurrencyID);
        jo.put("BaseCurrencyName", baseCurrencyName);
        jo.put("BaseCurrencySymbol", baseCurrencySymbol);
        jo.put("BaseExchangeRate", baseExchangeRate);
        jo.put("CapexID", capexID);
        jo.put("CapexLineItemID", capexLineItemID);
        jo.put("CapexLineItemNumber", capexLineItemNumber);
        jo.put("CapexNumber", capexNumber);
        jo.put("CategoryID", categoryID);
        jo.put("CategoryName", categoryName);
        jo.put("CurrencyID", currencyID);
        jo.put("CurrencyName", currencyName);
        jo.put("CurrencySymbol", currencySymbol);
        jo.put("DateCreated", dateCreated);
        jo.put("Description", desc);
        jo.put("NumberOfQuotes", numberOfQoutes);
        jo.put("Quantity", quantity);
        jo.put("RequesterID", requesterID);
        jo.put("RequesterName", requesterName);
        jo.put("UnitCost", unitCost);
        jo.put("UOM", uom);
        jo.put("USDExchangeRate", usdExchangeRate);
        jo.put("UsefulLife", usefulLife);
        JSONArray jsonQoutations = new JSONArray();
        for(CapexLineItemQoutation qoutation :qoutations)
            jsonQoutations.put(qoutation.getJSONObject());
        jo.put("CapexLineItemQuotation", jsonQoutations);

        return jo;
    }

    public float getAmount(){ return amount; }
    public float getAmountInUSD(){ return amountInUSD; }
    public int getBaseCurrencyID(){ return baseCurrencyID; }
    public String getBaseCurrencyName(){ return baseCurrencyName; }
    public String getBaseCurrencySymbol(){ return baseCurrencySymbol; }
    public float getBaseExchangeRate(){ return baseExchangeRate; }
    public int getCapexID(){ return capexID; }
    public int getCapexLineItemID(){ return capexLineItemID; }
    public String getCapexNumber(){ return capexNumber; }
    public int getCategoryID(){ return categoryID; }
    public String getCategoryName(){ return categoryName; }
    public int getCurrencyID(){ return currencyID; }
    public String getCurrencyName(){ return currencyName; }
    public String getCurrencySymbol(){ return currencySymbol ; }
    public String getDateCreated(){ return dateCreated; }
    public String getDesc(){ return desc; }
    public int getNumberOfQoutes(){ return numberOfQoutes; }
    public float getQuantity(){ return quantity; }
    public int getRequesterID(){ return requesterID; }
    public String getRequesterName(){ return requesterName; }
    public float getUnitCost(){ return unitCost; }
    public float getUsdExchangeRate(){ return usdExchangeRate; }
    public float getUsefulLife(){ return usefulLife; }

}