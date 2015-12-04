package applusvelosi.projects.android.salt.models.capex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import applusvelosi.projects.android.salt.SaltApplication;
import applusvelosi.projects.android.salt.models.Document;
import applusvelosi.projects.android.salt.utils.OnlineGateway;

/**
 * Created by Velosi on 10/9/15.
 */
public class CapexLineItemQoutation {
    private boolean isActive;
    private float amount, amountInUSD;
    private int capexLineItemID, capexLineItemQoutationID, currencyID;
    private String currencyName;
    private ArrayList<Document> documents;
    private float exchangeRate;
    private int financingSchemeID;
    private String financingSchemeName;
    private boolean isPrimary;
    private String note, paymentTerm, supplierName;

    public CapexLineItemQoutation(JSONObject jsonQoutation) throws Exception{
        isActive = jsonQoutation.getBoolean("Active");
        amount = (float)jsonQoutation.getDouble("Amount");
        amountInUSD = (float)jsonQoutation.getDouble("AmountInUSD");
        capexLineItemID = jsonQoutation.getInt("CapexLineItemID");
        capexLineItemQoutationID = jsonQoutation.getInt("CapexLineItemQuotationID");
        currencyID = jsonQoutation.getInt("CurrencyID");
        currencyName = jsonQoutation.getString("CurrencyName");
        exchangeRate = (float)jsonQoutation.getDouble("ExchangeRate");
        financingSchemeID = jsonQoutation.getInt("FinancingSchemeID");
        financingSchemeName = jsonQoutation.getString("FinancingSchemeName");
        isPrimary = jsonQoutation.getBoolean("IsPrimary");
        note = jsonQoutation.getString("Note");
        paymentTerm = jsonQoutation.getString("PaymentTerm");
        supplierName = jsonQoutation.getString("SupplierName");

        JSONArray jsonDocs = jsonQoutation.getJSONArray("Documents");
        documents = new ArrayList<Document>();
        for(int i=0; i<jsonDocs.length(); i++)
            documents.add(new Document(jsonDocs.getJSONObject(i)));
    }

    public float getAmount(){ return amount; }
    public float getAmountInUSD(){ return amountInUSD; }
    public int getCapexLineItemID(){ return capexLineItemID; }
    public int getCapexLineItemQoutationID(){ return capexLineItemQoutationID; }
    public int  getCurrencyID(){ return currencyID; }
    public String getCurrencyName(){ return currencyName; }
    public ArrayList<Document> getDocuments(){ return documents; }
    public float getExchangeRate(){ return exchangeRate; }
    public int getFinancingSchemeID(){ return financingSchemeID; }
    public String getFinancingSchemeName(){ return financingSchemeName; }
    public boolean isPrimary(){ return isPrimary; }
    public String getNotes(){ return note; }
    public String getPaymentTerm(){ return paymentTerm; }
    public String getSupplierName(){ return supplierName; }

    public JSONObject getJSONObject() throws Exception{
        JSONObject jo = new JSONObject();
        jo.put("Active", isActive);
        jo.put("Amount", amount);
        jo.put("AmountInUSD", amountInUSD);
        jo.put("CapexLineItemID", capexLineItemID);
        jo.put("CapexLineItemQuotationID", capexLineItemQoutationID);
        jo.put("CurrencyID", currencyID);
        jo.put("CurrencyName", currencyName);
        jo.put("ExchangeRate", exchangeRate);
        jo.put("FinancingSchemeID", financingSchemeID);
        jo.put("FinancingSchemeName", financingSchemeName);
        jo.put("IsPrimary", isPrimary);
        jo.put("Note", note);
        jo.put("PaymentTerm", paymentTerm);
        jo.put("SupplierName", supplierName);
        JSONArray jsonDocs = new JSONArray();
        for(Document doc :documents)
            jsonDocs.put(doc.getJSONObject());
        jo.put("Documents", jsonDocs);

        return jo;
    }

}
