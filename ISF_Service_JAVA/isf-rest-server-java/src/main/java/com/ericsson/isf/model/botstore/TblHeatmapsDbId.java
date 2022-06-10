package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblHeatmapsDbId generated by hbm2java
 */
@Embeddable
public class TblHeatmapsDbId implements java.io.Serializable {

	private int id;
	private int orderId;
	private String heatMapsLevel;
	private String columnName;

	public TblHeatmapsDbId() {
	}

	public TblHeatmapsDbId(int id, int orderId, String heatMapsLevel, String columnName) {
		this.id = id;
		this.orderId = orderId;
		this.heatMapsLevel = heatMapsLevel;
		this.columnName = columnName;
	}

	@Column(name = "Id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "OrderId", nullable = false)
	public int getOrderId() {
		return this.orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	@Column(name = "HeatMapsLevel", nullable = false, length = 128)
	public String getHeatMapsLevel() {
		return this.heatMapsLevel;
	}

	public void setHeatMapsLevel(String heatMapsLevel) {
		this.heatMapsLevel = heatMapsLevel;
	}

	@Column(name = "ColumnName", nullable = false, length = 128)
	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TblHeatmapsDbId))
			return false;
		TblHeatmapsDbId castOther = (TblHeatmapsDbId) other;

		return (this.getId() == castOther.getId()) && (this.getOrderId() == castOther.getOrderId())
				&& ((this.getHeatMapsLevel() == castOther.getHeatMapsLevel())
						|| (this.getHeatMapsLevel() != null && castOther.getHeatMapsLevel() != null
								&& this.getHeatMapsLevel().equals(castOther.getHeatMapsLevel())))
				&& ((this.getColumnName() == castOther.getColumnName())
						|| (this.getColumnName() != null && castOther.getColumnName() != null
								&& this.getColumnName().equals(castOther.getColumnName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getId();
		result = 37 * result + this.getOrderId();
		result = 37 * result + (getHeatMapsLevel() == null ? 0 : this.getHeatMapsLevel().hashCode());
		result = 37 * result + (getColumnName() == null ? 0 : this.getColumnName().hashCode());
		return result;
	}

}
