package org.Interface;
/**
  Represents one request sent from the client to the server.
 * action   - what the client wants to do
 * entity   - which table/entity the request is for
 * id       - used for getById / delete / update
  jsonData -JSON version of CustomerProd,uct for insert/update
 */
/**
 * @author ALoka (primary)
 **/

public class ClientRequest {

    private String action;
    private String entity;
    private Integer id;
    private String jsonData;
    private String serviceType;


     // No-argument constructor needed for Jackson

    public ClientRequest()
    {}

    //full constructor
    public ClientRequest(String action, String entity, Integer id, String jsonData)
    {
        this.action = action;
        this.entity = entity;
        this.id = id;
        this.jsonData = jsonData;
    }

    //constructor including servicetype
    public ClientRequest(String action, String entity, Integer id, String jsonData, String serviceType) {
        this.action = action;
        this.entity = entity;
        this.id = id;
        this.jsonData = jsonData;
        this.serviceType = serviceType;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getEntity()
    {
        return entity;
    }

    public void setEntity(String entity)
    {
        this.entity = entity;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getJsonData()
    {
        return jsonData;
    }

    public void setJsonData(String jsonData)
    {
        this.jsonData = jsonData;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
