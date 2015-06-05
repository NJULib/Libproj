package serverside.entity;

public class ParameterInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int parameterId; // ������id��
	private int parameterType; // �������
	private int parameterAmount; // ��������
	private int parameterPeriod; // ��������
	private double parameterDailyfine; // ����ÿ�շ�����

	public ParameterInfo(int parameterId, int parameterType,
			int parameterAmount, int parameterPeriod,
			double parameterDailyfine) {

		this.parameterId = parameterId;
		this.parameterType = parameterType;
		this.parameterAmount = parameterAmount;
		this.parameterPeriod = parameterPeriod;
		this.parameterDailyfine = parameterDailyfine;
	}

	public int getParameterId() {
		return parameterId;
	}

	public void setParameterId(int parameterId) {
		this.parameterId = parameterId;
	}

	public int getParameterType() {
		return parameterType;
	}

	public void setParameterType(int parameterType) {
		this.parameterType = parameterType;
	}

	public int getParameterAmount() {
		return parameterAmount;
	}

	public void setParameterAmount(int parameterAmount) {
		this.parameterAmount = parameterAmount;
	}

	public int getParameterPeriod() {
		return parameterPeriod;
	}

	public void setParameterPeriod(int parameterPeriod) {
		this.parameterPeriod = parameterPeriod;
	}

	public double getParameterDailyfine() {
		return parameterDailyfine;
	}

	public void setParameterDailyfine(double parameterDailyfine) {
		this.parameterDailyfine = parameterDailyfine;
	}
}
