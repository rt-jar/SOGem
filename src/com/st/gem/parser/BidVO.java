package com.st.gem.parser;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;
@Data
public class BidVO {
	
	@Field(value="Bid Number",separator=":")
	private String bidNumber;
	@Field(value="Dated",separator=":")
	private String bidDate;
	@Field("Bid End Date/Time")
	private String bidEndDate;
	@Field("Bid Validity")
	private String bidValidity;
	@Field("Ministry/State Name")
	private String ministryStateName;
	@Field("Department Name")
	private String departmentName;
	@Field("Organization Name")
	private String organizationName;
	@Field("Office Name")
	private String officeName;
	@Field("Total Quantity")
	private String totalQuantity;
	@Field("Item Category")
	private String itemCategory;
	@Field("Experience with Gov. Required")
	private String expWithGov;
	@Field("Additional")
	private List<String> additionalDtl;
	
	@ToString.Exclude
	private String textContent;
	
	public List<String> getAdditionalDtl(){
		if(additionalDtl == null) {
			additionalDtl = new ArrayList<>();
		}
		return additionalDtl;
	}
	
}
