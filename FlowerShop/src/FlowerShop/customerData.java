package FlowerShop;

import java.sql.Date;

/**
 *
 * @author LENOVO
 * 
 */
public class customerData {
    
    private Integer customerId;
    private Integer flowerId;
    private String name;
    private Integer quantity;
    private Integer price;
    private Date date;
    
    public customerData(Integer customerId, Integer flowerId, String name
            , Integer quantity, Integer price, Date date){
        this.customerId = customerId;
        this.flowerId = flowerId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }
    
    public Integer getCustomerId(){
        return customerId;
    }
    public Integer getFlowerId(){
        return flowerId;
    }
    public String getName(){
        return name;
    }
    public Integer getQuantity(){
        return quantity;
    }
    public Integer getPrice(){
        return price;
    }
    public Date getDate(){
        return date;
    }
    
}
