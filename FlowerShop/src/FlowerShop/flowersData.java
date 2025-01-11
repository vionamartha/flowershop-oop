/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlowerShop;

import java.sql.Date;

/**
 *
 * @author LENOVO
 * 
 */
public class flowersData {
    
    private Integer flowerId;
    private String name;
    private String status;
    private Integer price;
    private Date date;
    private String image;
    
    public flowersData(Integer flowerId, String name, String status, Integer price
            , String image, Date date){
        this.flowerId = flowerId;
        this.name = name;
        this.status = status;
        this.price = price;
        this.date = date;
        this.image = image;
    }
    
    public Integer getFlowerId(){
        return flowerId;
    }
    public String getName(){
        return name;
    }
    public String getStatus(){
        return status;
    }
    public Integer getPrice(){
        return price;
    }
    public Date getDate(){
        return date;
    }
    public String getImage(){
        return image;
    }
    
}
