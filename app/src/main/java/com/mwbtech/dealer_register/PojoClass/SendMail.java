package com.mwbtech.dealer_register.PojoClass;

public class SendMail {

    private int CustID;

    private  String SubCategoryName;

    private  String ChildCategoryName;

    private  String ItemName;

    private String MailSubject;
    private String MailBody;

    public SendMail(int custID, String mailSubject, String mailBody) {
        CustID = custID;
        MailSubject = mailSubject;
        MailBody = mailBody;
    }

    public SendMail(int custID, String subCategoryName, String childCategoryName, String itemName, String mailSubject) {
        CustID = custID;
        SubCategoryName = subCategoryName;
        ChildCategoryName = childCategoryName;
        ItemName = itemName;
        MailSubject = mailSubject;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getMailSubject() {
        return MailSubject;
    }

    public void setMailSubject(String mailSubject) {
        MailSubject = mailSubject;
    }

    public String getMailBody() {
        return MailBody;
    }

    public void setMailBody(String mailBody) {
        MailBody = mailBody;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public String getChildCategoryName() {
        return ChildCategoryName;
    }

    public String getItemName() {
        return ItemName;
    }
}
