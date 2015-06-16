/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.ge.corporate.piwik.tracking;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.json.JsonValue;
import javax.xml.bind.DatatypeConverter;

/**
 * A class that implements the <a href="https://developer.piwik.org/api-reference/tracking-api">
 * Piwik Tracking HTTP API</a>.  These requests can be sent using {@link PiwikTracker}.
 * @author brettcsorba
 */
public class PiwikRequest{
    private static final String ACTION_NAME = "action_name";
    private static final String ACTION_TIME = "gt_ms";
    private static final String ACTION_URL = "url";
    private static final String API_VERSION = "apiv";
    private static final String AUTH_TOKEN = "token_auth";
    private static final String CAMPAIGN_KEYWORD = "_rck";
    private static final String CAMPAIGN_NAME = "_rcn";
    private static final String CHARACTER_SET = "cs";
    private static final String CONTENT_INTERACTION = "c_i";
    private static final String CONTENT_NAME = "c_n";
    private static final String CONTENT_PIECE = "c_p";
    private static final String CONTENT_TARGET = "c_t";
    private static final String CURRENT_HOUR = "h";
    private static final String CURRENT_MINUTE = "m";
    private static final String CURRENT_SECOND = "s";
    private static final String DEVICE_RESOLUTION = "res";
    private static final String DOWNLOAD_URL = "download";
    private static final String ECOMMERCE_DISCOUNT = "ec_dt";
    private static final String ECOMMERCE_ID = "ec_id";
    private static final String ECOMMERCE_ITEMS = "ec_items";
    private static final String ECOMMERCE_LAST_ORDER_TIMESTAMP = "_ects";
    private static final String ECOMMERCE_REVENUE = "revenue";
    private static final String ECOMMERCE_SHIPPING_COST = "ec_sh";
    private static final String ECOMMERCE_SUBTOTAL = "ec_st";
    private static final String ECOMMERCE_TAX = "ec_tx";
    private static final String EVENT_ACTION = "e_a";
    private static final String EVENT_CATEGORY = "e_c";
    private static final String EVENT_NAME = "e_n";
    private static final String EVENT_VALUE = "e_v";
    private static final String HEADER_ACCEPT_LANGUAGE = "lang";
    private static final String GOAL_ID = "idgoal";
    private static final String GOAL_REVENUE = "revenue";
    private static final String HEADER_USER_AGENT = "ua";
    private static final String NEW_VISIT = "new_visit";
    private static final String OUTLINK_URL = "link";
    private static final String PAGE_CUSTOM_VARIABLE = "cvar";
    private static final String PLUGIN_DIRECTOR = "dir";
    private static final String PLUGIN_FLASH = "fla";
    private static final String PLUGIN_GEARS = "gears";
    private static final String PLUGIN_JAVA = "java";
    private static final String PLUGIN_PDF = "pdf";
    private static final String PLUGIN_QUICKTIME = "qt";
    private static final String PLUGIN_REAL_PLAYER = "realp";
    private static final String PLUGIN_SILVERLIGHT = "ag";
    private static final String PLUGIN_WINDOWS_MEDIA = "wma";
    private static final String RANDOM_VALUE = "rand";
    private static final String REFERRER_URL = "urlref";
    private static final String REQUEST_DATETIME = "cdt";
    private static final String REQUIRED = "rec";
    private static final String RESPONSE_AS_IMAGE = "send_image";
    private static final String SEARCH_CATEGORY = "search_cat";
    private static final String SEARCH_QUERY = "search";
    private static final String SEARCH_RESULTS_COUNT = "search_count";
    private static final String SITE_ID = "idsite";
    private static final String TRACK_BOT_REQUESTS = "bots";
    private static final String USER_CUSTOM_VARIABLE = "_cvar";
    private static final String USER_ID = "uid";
    private static final String VISITOR_CITY = "city";
    private static final String VISITOR_COUNTRY = "country";
    private static final String VISITOR_CUSTOM_ID = "cid";
    private static final String VISITOR_FIRST_VISIT_TIMESTAMP = "_idts";
    private static final String VISITOR_ID = "_id";
    private static final String VISITOR_IP = "cip";
    private static final String VISITOR_LATITUDE = "lat";
    private static final String VISITOR_LONGITUDE = "long";
    private static final String VISITOR_PREVIOUS_VISIT_TIMESTAMP = "_viewts";
    private static final String VISITOR_REGION = "region";
    private static final String VISITOR_VISIT_COUNT = "_idvc";
    
    private static final long REQUEST_DATETIME_AUTH_LIMIT = 14400000L;
    
    private final Map<String, Object> parameters = new HashMap<>();
    
    /**
     * Create a new request from the id of the site being tracked and the full
     * url for the current action.  This constructor also sets:
     * <pre>
     * {@code
     * Required = true
     * Visior Id = random 16 character hex string
     * Random Value = random 20 character hex string
     * API version = 1
     * Response as Image = false
     * }
     * </pre>
     * Overwrite these values yourself as desired.
     * @param siteId the id of the website we're tracking a visit/action for
     * @param actionUrl the full URL for the current action
     */
    public PiwikRequest(Integer siteId, URL actionUrl){
        setParameter(SITE_ID, siteId);
        setBooleanParameter(REQUIRED, true);
        setParameter(ACTION_URL, actionUrl);
        setParameter(VISITOR_ID, getRandomHexString(16));
        setParameter(RANDOM_VALUE, getRandomHexString(20));
        setParameter(API_VERSION, "1");
        setBooleanParameter(RESPONSE_AS_IMAGE, false);
    }
    
    /**
     * Get the title of the action being tracked
     * @return the title of the action being tracked
     */
    
    public String getActionName(){
        return (String)getParameter(ACTION_NAME);
    }
    
    /**
     * Set the title of the action being tracked. It is possible to 
     * <a href="http://piwik.org/faq/how-to/#faq_62">use slashes / 
     * to set one or several categories for this action</a>.
     * For example, <strong>Help / Feedback </strong>
     * will create the Action <strong>Feedback</strong> in the category Help.
     *
     * @param actionName the title of the action to set
     */
    public void setActionName(String actionName){
        setParameter(ACTION_NAME, actionName);
    }
    
    /**
     * Get the amount of time it took the server to generate this action, in milliseconds.
     * @return the amount of time
     */
    public Long getActionTime(){
        return (Long)getParameter(ACTION_TIME);
    }
    
    /**
     * Set the amount of time it took the server to generate this action, in milliseconds.
     * This value is used to process the 
     * <a href=http://piwik.org/docs/page-speed/>Page speed report</a> 
     * <strong>Avg. generation time</strong> column in the Page URL and Page Title reports,
     * as well as a site wide running average of the speed of your server. 
     * @param actionTime the amount of time to set
     */
    public void setActionTime(Long actionTime){
        setParameter(ACTION_TIME, actionTime);
    }
    
    /**
     * Get the full URL for the current action.
     * @return the full URL
     */
    public URL getActionUrl(){
        return (URL)getParameter(ACTION_URL);
    }
    
    /**
     * Set the full URL for the current action.
     * @param actionUrl the full URL to set
     */
    public void setActionUrl(URL actionUrl){
        setParameter(ACTION_URL, actionUrl);
    }
    
    /**
     * Get the api version
     * @return the api version
     */
    public String getApiVersion(){
        return (String)getParameter(API_VERSION);
    }
    
    /**
     * Set the api version to use (currently always set to 1)
     * @param apiVersion the api version to set
     */
    public void setApiVersion(String apiVersion){
        setParameter(API_VERSION, apiVersion);
    }
    
    /**
     * Get the authorization key.
     * @return the authorization key
     */
    public String getAuthToken(){
        return (String)getParameter(AUTH_TOKEN);
    }
    
    /**
     * Set the 32 character authorization key used to authenticate the API request.
     * @param authToken the authorization key to set
     */
    public void setAuthToken(String authToken){
        if (authToken == null){
            throw new NullPointerException("AuthToken cannot be null.");
        }
        if (authToken.length() != 32){            
            throw new IllegalArgumentException(authToken+" is not 32 characters long.");
        }
        setParameter(AUTH_TOKEN, authToken);
    }

    /**
     * Verifies that AuthToken has been set for this request.  Will throw an
     * {@link IllegalStateException} if not.
     */
    public void verifyAuthTokenSet(){
        if (getAuthToken() == null){
            throw new IllegalStateException("AuthToken must be set before this value can be set.");
        }
    }
    
    /**
     * Get the campaign keyword
     * @return the campaign keyword
     */
    public String getCampaignKeyword(){
        return (String)getParameter(CAMPAIGN_KEYWORD);
    }
    
    /**
     * Set the Campaign Keyword (see 
     * <a href=http://piwik.org/docs/tracking-campaigns/>Tracking Campaigns</a>). 
     * Used to populate the <em>Referrers &gt; Campaigns</em> report (clicking on a 
     * campaign loads all keywords for this campaign). <em>Note: this parameter 
     * will only be used for the first pageview of a visit.</em>
     * @param campaignKeyword the campaign keyword to set
     */
    public void setCampaignKeyword(String campaignKeyword){
        setParameter(CAMPAIGN_KEYWORD, campaignKeyword);
    }
    
    /**
     * Get the campaign name
     * @return the campaign name
     */
    public String getCampaignName(){
        return (String)getParameter(CAMPAIGN_NAME);
    }
    
    /**
     * Set the Campaign Name (see 
     * <a href=http://piwik.org/docs/tracking-campaigns/>Tracking Campaigns</a>). 
     * Used to populate the <em>Referrers &gt; Campaigns</em> report. <em>Note: this parameter 
     * will only be used for the first pageview of a visit.</em>
     * @param campaignName the campaign name to set
     */
    public void setCampaignName(String campaignName){
        setParameter(CAMPAIGN_NAME, campaignName);
    }
    
    /**
     * Get the charset of the page being tracked
     * @return the charset 
     */
    public Charset getCharacterSet(){
        return (Charset)getParameter(CHARACTER_SET);
    }
    
    /**
     * The charset of the page being tracked. Specify the charset if the data 
     * you send to Piwik is encoded in a different character set than the default
     * <strong>utf-8</strong>.
     * @param characterSet the charset to set
     */
    public void setCharacterSet(Charset characterSet){
        setParameter(CHARACTER_SET, characterSet);
    }
    
    /**
     * Get the name of the interaction with the content
     * @return the name of the interaction
     */
    public String getContentInteraction(){
        return (String)getParameter(CONTENT_INTERACTION);
    }
    
    /**
     * Set the name of the interaction with the content. For instance a 'click'.
     * @param contentInteraction the name of the interaction to set
     */
    public void setContentInteraction(String contentInteraction){
        setParameter(CONTENT_INTERACTION, contentInteraction);
    }
    
    /**
     * Get the name of the content
     * @return the name
     */
    public String getContentName(){
        return (String)getParameter(CONTENT_NAME);
    }
    
    /**
     * Set the name of the content. For instance 'Ad Foo Bar'.
     * @param contentName the name to set
     */
    public void setContentName(String contentName){
        setParameter(CONTENT_NAME, contentName);
    }
    
    /**
     * Get the content piece.
     * @return the content piece.
     */
    public String getContentPiece(){
        return (String)getParameter(CONTENT_PIECE);
    }
    
    /**
     * Set the actual content piece. For instance the path to an image, video, audio, any text.
     * @param contentPiece the content piece to set
     */
    public void setContentPiece(String contentPiece){
        setParameter(CONTENT_PIECE, contentPiece);
    }
    /**
     * Get the content target
     * @return the target
     */
    public URL getContentTarget(){
        return (URL)getParameter(CONTENT_TARGET);
    }
    
    /**
     * Set the target of the content. For instance the URL of a landing page.
     * @param contentTarget the target to set
     */
    public void setContentTarget(URL contentTarget){
        setParameter(CONTENT_TARGET, contentTarget);
    }
    
    /**
     * Get the current hour.
     * @return the current hour
     */
    public Integer getCurrentHour(){
        return (Integer)getParameter(CURRENT_HOUR);
    }
    
    /**
     * Set the current hour (local time).
     * @param currentHour the hour to set
     */
    public void setCurrentHour(Integer currentHour){
        setParameter(CURRENT_HOUR, currentHour);
    }
    
    /**
     * Get the current minute.
     * @return the current minute
     */
    public Integer getCurrentMinute(){
        return (Integer)getParameter(CURRENT_MINUTE);
    }
    
    /**
     * Set the current minute (local time).
     * @param currentMinute the minute to set
     */
    public void setCurrentMinute(Integer currentMinute){
        setParameter(CURRENT_MINUTE, currentMinute);
    }
    
    /**
     * Get the current second
     * @return the current second
     */
    public Integer getCurrentSecond(){
        return (Integer)getParameter(CURRENT_SECOND);
    }
    
    /**
     * Set the current second (local time).
     * @param currentSecond the second to set
     */
    public void setCurrentSecond(Integer currentSecond){
        setParameter(CURRENT_SECOND, currentSecond);
    }
    
    /**
     * Get the resolution of the device
     * @return the resolution
     */
    public String getDeviceResolution(){
        return (String)getParameter(DEVICE_RESOLUTION);
    }
    
    /**
     * Set the resolution of the device the visitor is using, eg <strong>1280x1024</strong>.
     * @param deviceResolution the resolution to set
     */
    public void setDeviceResolution(String deviceResolution){
        setParameter(DEVICE_RESOLUTION, deviceResolution);
    }
    
    /**
     * Get the url of a file the user had downloaded
     * @return the url
     */
    public URL getDownloadUrl(){
        return (URL)getParameter(DOWNLOAD_URL);
    }
    
    /**
     * Set the url of a file the user has downloaded. Used for tracking downloads.
     * We recommend to also set the <strong>url</strong> parameter to this same value.
     * @param downloadUrl the url to set
     */
    public void setDownloadUrl(URL downloadUrl){
        setParameter(DOWNLOAD_URL, downloadUrl);
    }
    
    /**
     * Sets <em>idgoal&#61;0</em> in the request to track an ecommerce interaction: 
     * cart update or an ecommerce order.
     */
    public void enableEcommerce(){
        setGoalId("0");
    }
    
    /**
     * Verifies that Ecommerce has been enabled for the request.  Will throw an
     * {@link IllegalStateException} if not.
     */
    public void verifyEcommerceEnabled(){
        if (!"0".equals(getGoalId())){
            throw new IllegalStateException("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
        }
    }
    
    /**
     * Verifies that Ecommerce has been enabled and that Ecommerce Id and 
     * Ecommerce Revenue have been set for the request.  Will throw an
     * {@link IllegalStateException} if not.
     *
     */
    public void verifyEcommerceState(){
        verifyEcommerceEnabled();
        if (getEcommerceId() == null){
            throw new IllegalStateException("EcommerceId must be set before this value can be set.");
        }
        if (getEcommerceRevenue() == null){
            throw new IllegalStateException("EcommerceRevenue must be set before this value can be set.");
        }
    }
    
    /**
     * Get the discount offered.
     * @return the discount
     */
    public Double getEcommerceDiscount(){
        return (Double)getParameter(ECOMMERCE_DISCOUNT);
    }
    
    /**
     * Set the discount offered.  Ecommerce must be enabled, and EcommerceId and
     * EcommerceRevenue must first be set.
     * @param discount the discount to set
     */
    public void setEcommerceDiscount(Double discount){
        verifyEcommerceState();
        setParameter(ECOMMERCE_DISCOUNT, discount);
    }
    
    /**
     * Get the id of this order.
     * @return the id
     */
    public String getEcommerceId(){
        return (String)getParameter(ECOMMERCE_ID);
    }
    
    /**
     * Set the unique string identifier for the ecommerce order (required when 
     * tracking an ecommerce order).  Ecommerce must be enabled.
     * @param id the id to set
     */
    public void setEcommerceId(String id){
        verifyEcommerceEnabled();
        setParameter(ECOMMERCE_ID, id);
    }
    
    /**
     * Get the {@link EcommerceItem} at the specified index
     * @param index the index of the {@link EcommerceItem} to return
     * @return the {@link EcommerceItem} at the specified index
     */
    public EcommerceItem getEcommerceItem(int index){
        return (EcommerceItem)getFromJsonArray(ECOMMERCE_ITEMS, index);
    }
    
    /**
     * Add an {@link EcommerceItem} to this order.  Ecommerce must be enabled,
     * and EcommerceId and EcommerceRevenue must first be set.
     * @param item the {@link EcommerceItem} to add
     */
    public void addEcommerceItem(EcommerceItem item){
        verifyEcommerceState();
        addToJsonArray(ECOMMERCE_ITEMS, item);
    }
    
    /**
     * Get the timestamp of the customer's last ecommerce order
     * @return the timestamp
     */
    public Long getEcommerceLastOrderTimestamp(){
        return (Long)getParameter(ECOMMERCE_LAST_ORDER_TIMESTAMP);
    }
    
    /**
     * Set the UNUX timestamp of this customer's last ecommerce order. This value
     * is used to process the "Days since last order" report.  Ecommerce must be
     * enabled, and EcommerceId and EcommerceRevenue must first be set.
     * @param timestamp the timestamp to set
     */
    public void setEcommerceLastOrderTimestamp(Long timestamp){
        verifyEcommerceState();
        setParameter(ECOMMERCE_LAST_ORDER_TIMESTAMP, timestamp);
    }
    
    /**
     * Get the grand total of the ecommerce order.
     * @return the grand total
     */
    public Double getEcommerceRevenue(){
        return (Double)getParameter(ECOMMERCE_REVENUE);
    }
    
    /**
     * Set the grand total of the ecommerce order (required when tracking an 
     * ecommerce order).  Ecommerce must be enabled.
     * @param revenue the grand total to set
     */
    public void setEcommerceRevenue(Double revenue){
        verifyEcommerceEnabled();
        setParameter(ECOMMERCE_REVENUE, revenue);
    }
    
    /**
     * Get the shipping cost of the ecommerce order.
     * @return the shipping cost
     */
    public Double getEcommerceShippingCost(){
        return (Double)getParameter(ECOMMERCE_SHIPPING_COST);
    }
    
    /**
     * Set the shipping cost of the ecommerce order.  Ecommerce must be enabled, 
     * and EcommerceId and EcommerceRevenue must first be set.
     * @param shippingCost the shipping cost to set
     */
    public void setEcommerceShippingCost(Double shippingCost){
        verifyEcommerceState();
        setParameter(ECOMMERCE_SHIPPING_COST, shippingCost);
    }
    
    /**
     * Get the subtotal of the ecommerce order; excludes shipping.
     * @return the subtotal
     */
    public Double getEcommerceSubtotal(){
        return (Double)getParameter(ECOMMERCE_SUBTOTAL);
    }
    
    /**
     * Set the subtotal of the ecommerce order; excludes shipping.  Ecommerce 
     * must be enabled and EcommerceId and EcommerceRevenue must first be set.
     * @param subtotal the subtotal to set
     */
    public void setEcommerceSubtotal(Double subtotal){
        verifyEcommerceState();
        setParameter(ECOMMERCE_SUBTOTAL, subtotal);
    }
    
    /**
     * Get the tax amount of the ecommerce order.
     * @return the tax amount
     */
    public Double getEcommerceTax(){
        return (Double)getParameter(ECOMMERCE_TAX);
    }
    
    /**
     * Set the tax amount of the ecommerce order.  Ecommerce must be enabled, and
     * EcommerceId and EcommerceRevenue must first be set.
     * @param tax the tax amount to set
     */
    public void setEcommerceTax(Double tax){
        verifyEcommerceState();
        setParameter(ECOMMERCE_TAX, tax);
    }
    
    /**
     * Get the event action.
     * @return the event action
     */
    public String getEventAction(){
        return getNonEmptyStringParameter(EVENT_ACTION);
    }
    
    /**
     * Set the event action. Must not be empty. (eg. Play, Pause, Duration, 
     * Add Playlist, Downloaded, Clicked...).
     * @param eventAction the event action to set
     */
    public void setEventAction(String eventAction){
        setNonEmptyStringParameter(EVENT_ACTION, eventAction);
    }
    
    /**
     * Get the event category.
     * @return the event category
     */
    public String getEventCategory(){
        return getNonEmptyStringParameter(EVENT_CATEGORY);
    }
    
    /**
     * Set the event category. Must not be empty. (eg. Videos, Music, Games...).
     * @param eventCategory the event category to set
     */
    public void setEventCategory(String eventCategory){
        setNonEmptyStringParameter(EVENT_CATEGORY, eventCategory);
    }
    
    /**
     * Get the event name.
     * @return the event name
     */
    public String getEventName(){
        return (String)getParameter(EVENT_NAME);
    }
    
    /**
     * Set the event name. (eg. a Movie name, or Song name, or File name...).
     * @param eventName the event name to set
     */
    public void setEventName(String eventName){
        setParameter(EVENT_NAME, eventName);
    }
    
    /**
     * Get the event value.
     * @return the event value
     */
    public Number getEventValue(){
        return (Number)getParameter(EVENT_VALUE);
    }
    
    /**
     * Set the event value. Must be a float or integer value (numeric), not a string.
     * @param eventValue the event value to set
     */
    public void setEventValue(Number eventValue){
        setParameter(EVENT_VALUE, eventValue);
    }
    
    /**
     * Get the goal id
     * @return the goal id
     */
    public String getGoalId(){
        return (String)getParameter(GOAL_ID);
    }
    
    /**
     * Set the goal id.  If specified, the tracking request will trigger a 
     * conversion for the goal of the website being tracked with this id.
     * @param goalId the goal id to set
     */
    public void setGoalId(String goalId){
        setParameter(GOAL_ID, goalId);
    }
    
    /**
     * Get the goal revenue.
     * @return the goal revenue
     */
    public Double getGoalRevenue(){
        return (Double)getParameter(GOAL_REVENUE);
    }
    
    /**
     * Set a monetary value that was generated as revenue by this goal conversion.
     * Only used if idgoal is specified in the request.
     * @param goalRevenue the goal revenue to set
     */
    public void setGoalRevenue(Double goalRevenue){
        if (getGoalId() == null){
            throw new IllegalStateException("GoalId must be set before GoalRevenue can be set.");
        }
        setParameter(GOAL_REVENUE, goalRevenue);
    }
    
    /**
     * Get the Accept-Language HTTP header
     * @return the Accept-Language HTTP header
     */
    public String getHeaderAcceptLanguage(){
        return (String)getParameter(HEADER_ACCEPT_LANGUAGE);
    }
    
    /**
     * Set an override value for the <strong>Accept-Language</strong> HTTP header
     * field. This value is used to detect the visitor's country if 
     * <a href="http://piwik.org/faq/troubleshooting/#faq_65">GeoIP </a>is not enabled.
     * @param acceptLangage the Accept-Language HTTP header to set
     */
    public void setHeaderAcceptLanguage(String acceptLangage){
        setParameter(HEADER_ACCEPT_LANGUAGE, acceptLangage);
    }
    
    /**
     * Get the User-Agent HTTP header
     * @return the User-Agent HTTP header
     */
    public String getHeaderUserAgent(){
        return (String)getParameter(HEADER_USER_AGENT);
    }
    
    /**
     * Set an override value for the <strong>User-Agent</strong> HTTP header field.
     * The user agent is used to detect the operating system and browser used.
     * @param userAgent the User-Agent HTTP header tos et
     */
    public void setHeaderUserAgent(String userAgent){
        setParameter(HEADER_USER_AGENT, userAgent);
    }
    
    /**
     * Get if this request will force a new visit.
     * @return true if this request will force a new visit
     */
    public Boolean getNewVisit(){
        return getBooleanParameter(NEW_VISIT);
    }
    
    /**
     * If set to true, will force a new visit to be created for this action.
     * @param newVisit if this request will force a new visit
     */
    public void setNewVisit(Boolean newVisit){
        setBooleanParameter(NEW_VISIT, newVisit);
    }
    
    /**
     * Get the outlink url
     * @return the outlink url
     */
    public URL getOutlinkUrl(){
        return (URL)getParameter(OUTLINK_URL);
    }
    
    /**
     * Set an external URL the user has opened. Used for tracking outlink clicks.
     * We recommend to also set the <strong>url</strong> parameter to this same value.
     * @param outlinkUrl the outlink url to set
     */
    public void setOutlinkUrl(URL outlinkUrl){
        setParameter(OUTLINK_URL, outlinkUrl);
    }
    
    /**
     * Get the page custom variable at the specified key.
     * @param key the key of the variable to get
     * @return the variable at the specified key, null if key is not present
     */
    public String getPageCustomVariable(String key){
        return getJsonParameter(PAGE_CUSTOM_VARIABLE, key);
    }
    
    /**
     * Set a page custom variable at the specified key.
     * @param key the key of the variable to set
     * @param value the value of the variable to set at the specified key
     */
    public void setPageCustomVariable(String key, String value){
        setJsonParameter(PAGE_CUSTOM_VARIABLE, key, value);
    }
    
    /**
     * Check if the visitor has the Director plugin.
     * @return true if visitor has the Director plugin
     */
    public Boolean getPluginDirector(){
        return getBooleanParameter(PLUGIN_DIRECTOR);
    }
    
    /**
     * Set if the visitor has the Director plugin.
     * @param director true if the visitor has the Director plugin
     */
    public void setPluginDirector(Boolean director){
        setBooleanParameter(PLUGIN_DIRECTOR, director);
    }
    
    /**
     * Check if the visitor has the Flash plugin.
     * @return true if the visitor has the Flash plugin
     */
    public Boolean getPluginFlash(){
        return getBooleanParameter(PLUGIN_FLASH);
    }
    
    /**
     * Set if the visitor has the Flash plugin.
     * @param flash true if the visitor has the Flash plugin
     */
    public void setPluginFlash(Boolean flash){
        setBooleanParameter(PLUGIN_FLASH, flash);
    }
    
    /**
     * Check if the visitor has the Gears plugin.
     * @return true if the visitor has the Gears plugin
     */
    public Boolean getPluginGears(){
        return getBooleanParameter(PLUGIN_GEARS);
    }
    
    /**
     * Set if the visitor has the Gears plugin.
     * @param gears true if the visitor has the Gears plugin
     */
    public void setPluginGears(Boolean gears){
        setBooleanParameter(PLUGIN_GEARS, gears);
    }
    
    /**
     * Check if the visitor has the Java plugin.
     * @return true if the visitor has the Java plugin
     */
    public Boolean getPluginJava(){
        return getBooleanParameter(PLUGIN_JAVA);
    }
    
    /**
     * Set if the visitor has the Java plugin.
     * @param java true if the visitor has the Java plugin
     */
    public void setPluginJava(Boolean java){
        setBooleanParameter(PLUGIN_JAVA, java);
    }
    
    /**
     * Check if the visitor has the PDF plugin.
     * @return true if the visitor has the PDF plugin
     */
    public Boolean getPluginPDF(){
        return getBooleanParameter(PLUGIN_PDF);
    }
    
    /**
     * Set if the visitor has the PDF plugin.
     * @param pdf true if the visitor has the PDF plugin
     */
    public void setPluginPDF(Boolean pdf){
        setBooleanParameter(PLUGIN_PDF, pdf);
    }
    
    /**
     * Check if the visitor has the Quicktime plugin.
     * @return true if the visitor has the Quicktime plugin
     */
    public Boolean getPluginQuicktime(){
        return getBooleanParameter(PLUGIN_QUICKTIME);
    }
    
    /**
     * Set if the visitor has the Quicktime plugin.
     * @param quicktime true if the visitor has the Quicktime plugin
     */
    public void setPluginQuicktime(Boolean quicktime){
        setBooleanParameter(PLUGIN_QUICKTIME, quicktime);
    }
    
    /**
     * Check if the visitor has the RealPlayer plugin.
     * @return true if the visitor has the RealPlayer plugin
     */
    public Boolean getPluginRealPlayer(){
        return getBooleanParameter(PLUGIN_REAL_PLAYER);
    }
    
    /**
     * Set if the visitor has the RealPlayer plugin.
     * @param realPlayer true if the visitor has the RealPlayer plugin
     */
    public void setPluginRealPlayer(Boolean realPlayer){
        setBooleanParameter(PLUGIN_REAL_PLAYER, realPlayer);
    }
    
    /**
     * Check if the visitor has the Silverlight plugin.
     * @return true if the visitor has the Silverlight plugin
     */
    public Boolean getPluginSilverlight(){
        return getBooleanParameter(PLUGIN_SILVERLIGHT);
    }
    
    /**
     * Set if the visitor has the Silverlight plugin.
     * @param silverlight true if the visitor has the Silverlight plugin
     */
    public void setPluginSilverlight(Boolean silverlight){
        setBooleanParameter(PLUGIN_SILVERLIGHT, silverlight);
    }
    
    /**
     * Check if the visitor has the Windows Media plugin.
     * @return true if the visitor has the Windows Media plugin
     */
    public Boolean getPluginWindowsMedia(){
        return getBooleanParameter(PLUGIN_WINDOWS_MEDIA);
    }
    
    /**
     * Set if the visitor has the Windows Media plugin.
     * @param windowsMedia true if the visitor has the Windows Media plugin
     */
    public void setPluginWindowsMedia(Boolean windowsMedia){
        setBooleanParameter(PLUGIN_WINDOWS_MEDIA, windowsMedia);
    }
    
    /**
     * Get the random value for this request
     * @return the random value
     */
    public String getRandomValue(){
        return (String)getParameter(RANDOM_VALUE);
    }
    
    /**
     * Set a random value that is generated before each request. Using it helps 
     * avoid the tracking request being cached by the browser or a proxy.
     * @param randomValue the random value to set
     */
    public void setRandomValue(String randomValue){
        setParameter(RANDOM_VALUE, randomValue);
    }
    
    /**
     * Get the referrer url
     * @return the referrer url
     */
    public URL getReferrerUrl(){
        return (URL)getParameter(REFERRER_URL);
    }
    
    /**
     * Set the full HTTP Referrer URL. This value is used to determine how someone
     * got to your website (ie, through a website, search engine or campaign).
     * @param refferrerUrl the referrer url to set
     */
    public void setReferrerUrl(URL refferrerUrl){
        setParameter(REFERRER_URL, refferrerUrl);
    }
    
    /**
     * Get the datetime of the request
     * @return the datetime of the request
     */
    public PiwikDate getRequestDatetime(){
        return (PiwikDate)getParameter(REQUEST_DATETIME);
    }
    
    /**
     * Set the datetime of the request (normally the current time is used). 
     * This can be used to record visits and page views in the past. The datetime
     * must be sent in UTC timezone. <em>Note: if you record data in the past, you will
     * need to <a href="http://piwik.org/faq/how-to/#faq_59">force Piwik to re-process
     * reports for the past dates</a>.</em> If you set the <em>Request Datetime</em> to a datetime
     * older than four hours then <em>Auth Token</em> must be set. If you set 
     * <em>Request Datetime</em> with a datetime in the last four hours then you
     * don't need to pass <em>Auth Token</em>.
     * @param datetime the datetime of the request to set
     */
    public void setRequestDatetime(PiwikDate datetime){
        if (datetime == null){
            throw new NullPointerException("Datetime cannot be null.");            
        }
        if (new Date().getTime()-datetime.getTime() > REQUEST_DATETIME_AUTH_LIMIT && getAuthToken() == null){
            throw new IllegalStateException("Because you are trying to set RequestDatetime for a time greater than 4 hours ago, AuthToken must be set first.");
            
        }
        setParameter(REQUEST_DATETIME, datetime);
    }
    
    /**
     * Get if this request will be tracked.
     * @return true if request will be tracked
     */
    public Boolean getRequired(){
        return getBooleanParameter(REQUIRED);
    }
    
    /**
     * Set if this request will be tracked by the Piwik server.
     * @param required true if request will be tracked
     */
    public void setRequired(Boolean required){
        setBooleanParameter(REQUIRED, required);
    }
    
    /**
     * Get if the response will be an image.
     * @return true if the response will be an an image
     */
    public Boolean getResponseAsImage(){
        return getBooleanParameter(RESPONSE_AS_IMAGE);
    }
    
    /**
     * Set if the response will be an image.  If set to false, Piwik will respond
     * with a HTTP 204 response code instead of a GIF image. This improves performance
     * and can fix errors if images are not allowed to be obtained directly
     * (eg Chrome Apps). Available since Piwik 2.10.0.

     * @param responseAsImage true if the response will be an image
     */
    public void setResponseAsImage(Boolean responseAsImage){
        setBooleanParameter(RESPONSE_AS_IMAGE, responseAsImage);
    }
    
    /**
     * Get the search category
     * @return the search category
     */
    public String getSearchCategory(){
        return (String)getParameter(SEARCH_CATEGORY);
    }
    
    /**
     * When <strong>Query</strong> is specified, you can optionally specify a search category
     * with this parameter.
     * @param searchCategory the search category to set
     */
    public void setSearchCategory(String searchCategory){
        if (getSearchQuery() == null){
            throw new IllegalStateException("SearchQuery must be set before SearchCategory can be set.");
        }
        setParameter(SEARCH_CATEGORY, searchCategory);
    }
    
    /**
     * Get the search query.
     * @return the search query
     */
    public String getSearchQuery(){
        return (String)getParameter(SEARCH_QUERY);
    }
    
    /**
     * Set the search query.  When specified, the request will not be tracked as
     * a normal pageview but will instead be tracked as a Site Search request.
     * @param searchQuery the search query to set
     */
    public void setSearchQuery(String searchQuery){
        setParameter(SEARCH_QUERY, searchQuery);
    }
    
    /**
     * Get the search results count.
     * @return the search results count
     */
    public Long getSearchResultsCount(){
        return (Long)getParameter(SEARCH_RESULTS_COUNT);
    }
    
    /**
     * When <strong>Query</strong> is specified, we also recommend to set the 
     * search count to the number of search results displayed on the results page. 
     * When keywords are tracked with {@code Search Results Count=0} they will appear in 
     * the "No Result Search Keyword" report.
     * @param searchResultsCount the search results count to set
     */
    public void setSearchResultsCount(Long searchResultsCount){
        if (getSearchQuery() == null){
            throw new IllegalStateException("SearchQuery must be set before SearchResultsCount can be set.");
        }
        setParameter(SEARCH_RESULTS_COUNT, searchResultsCount);
    }
    
    /**
     * Get the id of the website we're tracking.
     * @return the id of the website
     */
    public Integer getSiteId(){
        return (Integer)getParameter(SITE_ID);
    } 
    
    /**
     * Set the ID of the website we're tracking a visit/action for.
     * @param siteId the id of the website to set
     */
    public void setSiteId(Integer siteId){
        setParameter(SITE_ID, siteId);
    }
    
    /**
     * Set if bot requests should be tracked
     * @return true if bot requests should be tracked
     */
    public Boolean getTrackBotRequests(){
        return getBooleanParameter(TRACK_BOT_REQUESTS);
    }
    
    /**
     * By default Piwik does not track bots. If you use the Tracking Java API,
     * you may be interested in tracking bot requests. To enable Bot Tracking in
     * Piwik, set <em>Track Bot Requests</em> to true.
     * @param trackBotRequests true if bot requests should be tracked
     */
    public void setTrackBotRequests(Boolean trackBotRequests){
        setBooleanParameter(TRACK_BOT_REQUESTS, trackBotRequests);
    }
    
    /**
     * Get the user custom variable at the specified key.
     * @param key the key of the variable to get
     * @return the variable at the specified key, null if key is not present
     */
    public String getUserCustomVariable(String key){
        return getJsonParameter(USER_CUSTOM_VARIABLE, key);
    }
    
    /**
     * Set a user custom variable at the specified key.
     * @param key the key of the variable to set
     * @param value the value of the variable to set at the specified key
     */
    public void setUserCustomVariable(String key, String value){
        setJsonParameter(USER_CUSTOM_VARIABLE, key, value);
    }
    
    /**
     * Get the user id for this request.
     * @return the user id
     */
    public String getUserId(){
        return (String)getParameter(USER_ID);
    }
    
    /**
     * Set the <a href="http://piwik.org/docs/user-id/">user id</a> for this request.
     * User id is any non empty unique string identifying the user (such as an email
     * address or a username). To access this value, users must be logged-in in your
     * system so you can fetch this user id from your system, and pass it to Piwik. 
     * The user id appears in the visitor log, the Visitor profile, and you can 
     * <a href="http://developer.piwik.org/api-reference/segmentation">Segment</a>
     * reports for one or several user ids. When specified, the user id will be
     * "enforced". This means that if there is no recent visit with this user id,
     * a new one will be created. If a visit is found in the last 30 minutes with
     * your specified user id, then the new action will be recorded to this existing visit.
     * @param userId the user id to set
     */
    public void setUserId(String userId){
        setNonEmptyStringParameter(USER_ID, userId);
    }
    
    /**
     * Get the visitor's city.
     * @return the visitor's city
     */
    public String getVisitorCity(){
        return (String)getParameter(VISITOR_CITY);
    }
    
    /**
     * Set an override value for the city. The name of the city the visitor is 
     * located in, eg, Tokyo.  AuthToken must first be set.
     * @param city the visitor's city to set
     */
    public void setVisitorCity(String city){
        verifyAuthTokenSet();
        setParameter(VISITOR_CITY, city);
    }
    
    /**
     * Get the visitor's country.
     * @return the visitor's country
     */
    public PiwikLocale getVisitorCountry(){
        return (PiwikLocale)getParameter(VISITOR_COUNTRY);
    }
    
    /**
     * Set an override value for the country.  AuthToken must first be set.
     * @param country the visitor's country to set
     */
    public void setVisitorCountry(PiwikLocale country){
        verifyAuthTokenSet();
        setParameter(VISITOR_COUNTRY, country);
    }
    
    /**
     * Get the visitor's custom id.
     * @return the visitor's custom id
     */
    public String getVisitorCustomId(){
        return (String)getParameter(VISITOR_CUSTOM_ID);
    }
    
    /**
     * Set a custom visitor ID for this request. You must set this value to exactly
     * a 16 character hexadecimal string (containing only characters 01234567890abcdefABCDEF).
     * We recommended to set the UserId rather than the VisitorCustomId.
     * @param visitorCustomId the visitor's custom id to set
     */
    public void setVisitorCustomId(String visitorCustomId){
        if (visitorCustomId == null){
            throw new NullPointerException("VisitorCustomId cannot be null.");
        }
        if (visitorCustomId.length() != 16){            
            throw new IllegalArgumentException(visitorCustomId+" is not 16 characters long.");
        }
        // Verify visitorID is a 16 character hexadecimal string
        if (!visitorCustomId.matches("[0-9A-Fa-f]+")){
            throw new IllegalArgumentException(visitorCustomId+" is not a hexadecimal string.");
        }
        setParameter(VISITOR_CUSTOM_ID, visitorCustomId);
    }
    
    /**
     * Get the timestamp of the visitor's first visit.
     * @return the timestamp of the visitor's first visit
     */
    public Long getVisitorFirstVisitTimestamp(){
        return (Long)getParameter(VISITOR_FIRST_VISIT_TIMESTAMP);
    }
    
    /**
     * Set the UNIX timestamp of this visitor's first visit. This could be set 
     * to the date where the user first started using your software/app, or when
     * he/she created an account. This parameter is used to populate the 
     * <em>Goals &gt; Days to Conversion</em> report.
     * @param timestamp the timestamp of the visitor's first visit to set
     */
    public void setVisitorFirstVisitTimestamp(Long timestamp){
        setParameter(VISITOR_FIRST_VISIT_TIMESTAMP, timestamp);
    }
    
    /**
     * Get the visitor's id.
     * @return the visitor's id
     */
    public String getVisitorId(){
        return (String)getParameter(VISITOR_ID);
    }
    
    /**
     * Set the unique visitor ID, must be a 16 characters hexadecimal string. 
     * Every unique visitor must be assigned a different ID and this ID must not
     * change after it is assigned. If this value is not set Piwik will still 
     * track visits, but the unique visitors metric might be less accurate.
     * @param visitorId the visitor id to set
     */
    public void setVisitorId(String visitorId){
        if (visitorId == null){
            throw new NullPointerException("VisitorId cannot be null.");
        }
        if (visitorId.length() != 16){            
            throw new IllegalArgumentException(visitorId+" is not 16 characters long.");
        }
        // Verify visitorID is a 16 character hexadecimal string
        if (!visitorId.matches("[0-9A-Fa-f]+")){
            throw new IllegalArgumentException(visitorId+" is not a hexadecimal string.");
        }
        setParameter(VISITOR_ID, visitorId);
    }
    
    /**
     * Get the visitor's ip.
     * @return the visitor's ip
     */
    public String getVisitorIp(){
        return (String)getParameter(VISITOR_IP);
    }
    
    /**
     * Set the override value for the visitor IP (both IPv4 and IPv6 notations 
     * supported).  AuthToken must first be set.
     * @param visitorIp the visitor's ip to set
     */
    public void setVisitorIp(String visitorIp){
        verifyAuthTokenSet();
        setParameter(VISITOR_IP, visitorIp);
    }
    
    /**
     * Get the visitor's latitude.
     * @return the visitor's latitude
     */
    public Double getVisitorLatitude(){
        return (Double)getParameter(VISITOR_LATITUDE);
    }
    
    /**
     * Set an override value for the visitor's latitude, eg 22.456.  AuthToken 
     * must first be set.
     * @param latitude the visitor's latitude to set
     */
    public void setVisitorLatitude(Double latitude){
        verifyAuthTokenSet();
        setParameter(VISITOR_LATITUDE, latitude);
    }
    
    /**
     * Get the visitor's longitude.
     * @return the visitor's longitude
     */
    public Double getVisitorLongitude(){
        return (Double)getParameter(VISITOR_LONGITUDE);
    }
    
    /**
     * Set an override value for the visitor's longitude, eg 22.456.  AuthToken 
     * must first be set.
     * @param longitude the visitor's longitude to set
     */
    public void setVisitorLongitude(Double longitude){
        verifyAuthTokenSet();
        setParameter(VISITOR_LONGITUDE, longitude);
    }
    
    /**
     * Get the timestamp of the visitor's previous visit.
     * @return the timestamp of the visitor's previous visit
     */
    public Long getVisitorPreviousVisitTimestamp(){
        return (Long)getParameter(VISITOR_PREVIOUS_VISIT_TIMESTAMP);
    }
    
    /**
     * Set the UNIX timestamp of this visitor's previous visit. This parameter 
     * is used to populate the report 
     * <em>Visitors &gt; Engagement &gt; Visits</em> by days since last visit.
     * @param timestamp the timestamp of the visitor's previous visit to set
     */
    public void setVisitorPreviousVisitTimestamp(Long timestamp){
        setParameter(VISITOR_PREVIOUS_VISIT_TIMESTAMP, timestamp);
    }
    
    /**
     * Get the visitor's region.
     * @return the visitor's region
     */
    public String getVisitorRegion(){
        return (String)getParameter(VISITOR_REGION);
    }
    
    /**
     * Set an override value for the region. Should be set to the two letter 
     * region code as defined by 
     * <a href="http://www.maxmind.com/?rId=piwik">MaxMind's</a> GeoIP databases.
     * See <a href="http://dev.maxmind.com/static/maxmind-region-codes.csv">here</a>
     * for a list of them for every country (the region codes are located in the 
     * second column, to the left of the region name and to the right of the country
     * code).
     * @param region the visitor's region to set
     */
    public void setVisitorRegion(String region){
        verifyAuthTokenSet();
        setParameter(VISITOR_REGION, region);
    }
    
    /**
     * Get the count of visits for this visitor.
     * @return the count of visits for this visitor
     */
    public Integer getVisitorVisitCount(){
        return (Integer)getParameter(VISITOR_VISIT_COUNT);
    }
    
    /**
     * Set the current count of visits for this visitor. To set this value correctly,
     * it would be required to store the value for each visitor in your application
     * (using sessions or persisting in a database). Then you would manually increment
     * the counts by one on each new visit or "session", depending on how you choose
     * to define a visit. This value is used to populate the report 
     * <em>Visitors &gt; Engagement &gt; Visits by visit number</em>.
     * @param visitorVisitCount the count of visits for this visitor to set
     */
    public void setVisitorVisitCount(Integer visitorVisitCount){
        setParameter(VISITOR_VISIT_COUNT, visitorVisitCount);
    }
    
    /**
     * Get the query string represented by this object.
     * @return the query string represented by this object
     */
    
    public String getQueryString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> parameter : parameters.entrySet()){
            if (sb.length() > 0){
                sb.append("&");
            }
            sb.append(parameter.getKey());
            sb.append("=");
            sb.append(parameter.getValue().toString());
        }
        
        return sb.toString();
    }
    
    /**
     * Get the url encoded query string represented by this object.
     * @return the url encoded query string represented by this object
     */
    public String getUrlEncodedQueryString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> parameter : parameters.entrySet()){
            if (sb.length() > 0){
                sb.append("&");
            }
            sb.append(parameter.getKey());
            sb.append("=");
            sb.append(URLEncoder.encode(parameter.getValue().toString()));
        }
        
        return sb.toString();
    }
    
    /**
     * Get a random hexadecimal string of a specified length.
     * @param length length of the string to produce
     * @return a random string consisting only of hexadecimal characters
     */
    public static String getRandomHexString(int length){
        byte[] bytes = new byte[length/2];
        new Random().nextBytes(bytes);
        return DatatypeConverter.printHexBinary(bytes);
    }
    
    /**
     * Get a stored parameter.
     * @param key the parameter's key
     * @return the stored parameter's value
     */
    private Object getParameter(String key){   
        return parameters.get(key);
    }
    
    /**
     * Set a stored parameter.
     * @param key the parameter's key
     * @param value the parameter's value.  Cannot be null
     */
    private void setParameter(String key, Object value){
        if (value == null){
            throw new NullPointerException("Value cannot be null.");
        }        
        parameters.put(key, value);
    }
    
    /**
     * Get a stored parameter that is a non-empty string.
     * @param key the parameter's key
     * @return the stored parameter's value
     */
    private String getNonEmptyStringParameter(String key){       
        return (String)parameters.get(key);
    }
    
    /**
     * Set a stored parameter and verify it is a non-empty string.
     * @param key the parameter's key
     * @param value the parameter's value.  Cannot be null.  Cannot be the empty
     * string
     */
    private void setNonEmptyStringParameter(String key, String value){
        if (value == null){
            throw new NullPointerException("Value cannot be null.");
        }
        if (value.length() == 0){
            throw new IllegalArgumentException("Value cannot be empty.");
        }
        parameters.put(key, value);
    }
    
    /**
     * Get a stored parameter that is a boolean.
     * @param key the parameter's key
     * @return the stored parameter's value
     */
    private Boolean getBooleanParameter(String key){        
        Integer i = (Integer)parameters.get(key);
        return i.equals(1);
    }
    
    /**
     * Set a stored parameter that is a boolean.  This value will be stored as "1"
     * for true and "0" for false.
     * @param key the parameter's key
     * @param value the parameter's value.  Cannot be null
     */
    private void setBooleanParameter(String key, Boolean value){
        if (value == null){
            throw new NullPointerException("Value cannot be null.");
        }
        if (value){
            parameters.put(key, 1);
        }
        else{
            parameters.put(key, 0);            
        }
    }
    
    /**
     * Get a value that is stored in a json object at the specified parameter.
     * @param parameter the parameter to retrieve the json object from
     * @param key the key of the value.  Cannot be null
     * @return the value
     */
    private String getJsonParameter(String parameter, String key){
        if (key == null){
            throw new NullPointerException("Key cannot be null.");
        }
        
        PiwikJsonObject o = (PiwikJsonObject)parameters.get(parameter);
        if (o == null){
            return null;
        }
        
        return o.get(key);
    }
    
    /**
     * Store a value in a json object at the specified parameter.
     * @param parameter the parameter to store the json object at
     * @param key the key of the value.  Cannot be null
     * @param value the value.  Cannot be null
     */
    private void setJsonParameter(String parameter, String key, String value){
        if (key == null){
            throw new NullPointerException("Key cannot be null.");
        }
        if (value == null){
            throw new NullPointerException("Value cannot be null.");
        }
        
        PiwikJsonObject o = (PiwikJsonObject)parameters.get(parameter);
        if (o == null){
            o = new PiwikJsonObject();
            parameters.put(parameter, o);
        }
        o.put(key, value);
    }
    
    /**
     * Get the value at the specified index from the json array at the specified
     * parameter.
     * @param parameter the parameter of the json array to access
     * @param index the index of the value in the json array
     * @return the value at the index in the json array
     */
    private JsonValue getFromJsonArray(String parameter, int index){
        PiwikJsonArray a = (PiwikJsonArray)parameters.get(parameter);
        if (a == null){
            return null;
        }
        
        return a.get(index);
    }
    /**
     * Add a value to the json array at the specified parameter
     * @param parameter the parameter of the json array to add to
     * @param value the value to add.  Cannot be null
     */
    private void addToJsonArray(String parameter, JsonValue value){
        if (value == null){
            throw new NullPointerException("Value cannot be null.");
        }
        
        PiwikJsonArray a = (PiwikJsonArray)parameters.get(parameter);
        if (a == null){
            a = new PiwikJsonArray();
            parameters.put(parameter, a);
        }
        a.add(value);
    }
}
