package com.ericsson.isf.mqttclient.dto;

public class MqttPublishModel {

	private String envName;
    private Object message;
    private boolean retained;
    private Integer qos;
    private String signum;

    public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }


    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }

	public String getSignum() {
		return signum;
	}

	public void setSignum(String signum) {
		this.signum = signum;
	}

	
	public boolean isRetained() {
		return retained;
	}

	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	@Override
	public String toString() {
		return "MqttPublishModel [envName=" + envName + ", message=" + message + ", retained=" + retained + ", qos=" + qos
				+ ", signum=" + signum + "]";
	}
    
    
}
