package io.oferto.pochyperledgerapi.domain;

import java.util.Date;
	
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class Trade {
	@ApiModelProperty(notes = "Unique identifier of the trade. No two trades can have the same id.", example = "123e4567-e89b-42d3-a456-556642440000", required = true, position = 0)
	private String ID;
		
	@Size(max = 255)
    @ApiModelProperty(notes = "Trade owner.", example = "Miguel Salinas", required = true, position = 1)
	private String Owner;
        
    @NotBlank(message = "Trade type is mandatory")
    @ApiModelProperty(notes = "Trade type: Buy, Sell, Executed", example = "Sell", required = true, position = 2)    
	private String TradeType;
    
    @Min(value = 0, message = "Trade power value in Kw not be less than 0")
    @ApiModelProperty(notes = "Trade value", example = "5.4", required = true, position = 3)    
	private Float Value;

    @Min(value = 0, message = "Trade price value in euros not be less than 0")
    @ApiModelProperty(notes = "Trade price", example = "55.2", required = true, position = 4)    
	private Float Price;
        
    @ApiModelProperty(notes = "Trade creation date", example = "2021-04-03T19:32:39+00:00", required = true, position = 5)    
	private Date CreationDate;
    	
    @ApiModelProperty(notes = "Trade updated date", example = "2021-04-03T19:32:39+00:00", required = true, position = 6)    
	private Date UpdatedDate;
    
	public String getID() {
		return ID;
	}

	public void setId(String id) {
		this.ID = id;
	}

	public String getOwner() {
		return Owner;
	}

	public void setOwner(String owner) {
		this.Owner = owner;
	}

	public String getTradeType() {
		return TradeType;
	}

	public void setTradeType(String tradeType) {
		this.TradeType = tradeType;
	}

	public Float getValue() {
		return Value;
	}

	public void setValue(Float value) {
		this.Value = value;
	}

	public Float getPrice() {
		return Price;
	}

	public void setPrice(Float price) {
		this.Price = price;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.CreationDate = creationDate;
	}

	public Date getUpdatedDate() {
		return UpdatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.UpdatedDate = updatedDate;
	}
	
	@Override
	public String toString() {
		return "Trade [ID=" + ID + ", Owner=" + Owner + ", TradeType=" + TradeType + ", Value=" + Value + ", Price="
				+ Price + ", CreationDate=" + CreationDate + "]";
	}
}
