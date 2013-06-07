package com.softlink.datastore.model;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@Entity(name="finance-data")
@Index
public class FinanceData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	 @Id public Long request_id;
	 private Long cus_id;	
	 private Long assets_id;
	 @Unindex private Date init_time;
	 @Unindex private Date req_time;
	 private Date update_time;
	 @Unindex private int real_amount;
	 @Unindex private int bill_amount;
	 private String reporter;
	 private String requester;
	 private String manager;
	 private String status;
	 @Unindex private String comment;
	 @Unindex private String document;
	 @Unindex private String refference;
	 @Unindex private String tags;
	 private String account;
	 private String currency;
	 private String description;
	 private Integer version = 0;

	 public Integer getVersion() {
			return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	 public FinanceData() {
	 	super();	
	 }

	public Long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

	public Long getCus_id() {
		return cus_id;
	}

	public void setCus_id(Long cus_id) {
		this.cus_id = cus_id;
	}

	public Long getAssets_id() {
		return assets_id;
	}

	public void setAssets_id(Long assets_id) {
		this.assets_id = assets_id;
	}

	public Date getInit_time() {
		return init_time;
	}

	public void setInit_time(Date init_time) {
		this.init_time = init_time;
	}

	public Date getReq_time() {
		return req_time;
	}

	public void setReq_time(Date req_time) {
		this.req_time = req_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public int getReal_amount() {
		return real_amount;
	}

	public void setReal_amount(int real_amount) {
		this.real_amount = real_amount;
	}

	public int getBill_amount() {
		return bill_amount;
	}

	public void setBill_amount(int bill_amount) {
		this.bill_amount = bill_amount;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getRefference() {
		return refference;
	}

	public void setRefference(String refference) {
		this.refference = refference;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
