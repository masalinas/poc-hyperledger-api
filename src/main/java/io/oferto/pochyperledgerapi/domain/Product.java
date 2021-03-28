package io.oferto.pochyperledgerapi.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class Product {
	@ApiModelProperty(notes = "Unique identifier of the product. No two products can have the same id.", example = "asset1", required = true, position = 0)
	private String ID;
	
	@NotBlank(message = "product color is mandatory")
    @ApiModelProperty(notes = "Product color.", example = "blue", required = true, position = 1)
	private String Color;
	
	@Size(max = 255)
    @ApiModelProperty(notes = "Product owner.", example = "Tokomoto", required = true, position = 2)
	private String Owner;
    
    @Min(value = 0, message = "Product size not be less than 0")
    @ApiModelProperty(notes = "Product size", example = "5.4", required = true, position = 3)    
	private Float Size;
    
    @Min(value = 0, message = "Product appraised value not be less than 0")
    @ApiModelProperty(notes = "Product appraised value", example = "300", required = true, position = 3)    
	private Float AppraisedValue;
    
    @NotBlank(message = "Product documentation type is mandatory")
    @ApiModelProperty(notes = "Product documentation type", example = "asset", required = true, position = 3)    
	private String docType;
    
    public String getId() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String Color) {
		this.Color = Color;
	}

	public String getOwner() {
		return Owner;
	}

	public void setOwner(String Owner) {
		this.Owner = Owner;
	}

	public Float getSize() {
		return Size;
	}

	public void setSize(Float Size) {
		this.Size = Size;
	}

	public Float getAppraisedValue() {
		return AppraisedValue;
	}

	public void setAppraisedValue(Float AppraisedValue) {
		this.AppraisedValue = AppraisedValue;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}
	
	@Override
	public String toString() {
		return "Product [ID=" + ID + ", color=" + Color + ", Owner=" + Owner + ", Size=" + Size + ", appraisedValue="
				+ AppraisedValue + ", docType=" + docType + "]";
	}

}
