package com.softlink.financedatastore.client;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@Entity(name="finance-data")
@Index
/**
 * 
 * @author Tranan
 *
 */
public class FinanceRequirements implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FinanceRequirements(Long request_id, Long cus_id, Long assets_id,
			Date init_time, Date req_time, Date update_time, int real_amount,
			int tax_amount, String reporter, String requester, String manager,
			String status, String comment, String document, String refference,
			String tags, String account, String currency, String description) {
		super();
		this.request_id = request_id;
		this.cus_id = cus_id;
		this.assets_id = assets_id;
		this.init_time = init_time;
		this.req_time = req_time;
		this.update_time = update_time;
		this.real_amount = real_amount;
		this.tax_amount = tax_amount;
		this.reporter = reporter;
		this.requester = requester;
		this.manager = manager;
		this.status = status;
		this.comment = comment;
		this.document = document;
		this.refference = refference;
		this.tags = tags;
		this.account = account;
		this.currency = currency;
		this.description = description;
	}
	/**
	 * 
	 */
	@Id 
	 public Long request_id;
	 private Long cus_id;	
	 private Long assets_id;
	 @Unindex private Date init_time;
	 @Unindex private Date req_time;
	 private Date update_time;
	 @Unindex private int real_amount;
	 @Unindex private int tax_amount;
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

	 public FinanceRequirements() {
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

	public int getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(int tax_amount) {
		this.tax_amount = tax_amount;
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
