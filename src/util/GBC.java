package util;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {
	private static final long serialVersionUID = 1L;

	// gridx ָ�������������ʾ����ʼ�ߵĵ�Ԫ�������еĵ�һ����Ԫ��Ϊ gridx=0��
	// gridy ָ��λ�������ʾ����Ķ����ĵ�Ԫ���������ϱߵĵ�Ԫ��Ϊ gridy=0��
	public GBC(int gridx, int gridy) {
		this.gridx = gridx;
		this.gridy = gridy;
	}

	// gridwidth  ָ�������ʾ�����ĳһ���еĵ�Ԫ������
	// gridheight ָ���������ʾ�����һ���еĵ�Ԫ������
	public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
	}

	// anchor �����С������ʾ����ʱʹ�ô��ֶΡ�
	public GBC setAnchor(int anchor) {
		this.anchor = anchor;
		return this;
	}

	// fill ���������ʾ������������������ʾ����Ĵ�Сʱʹ�ô��ֶΡ�
	public GBC setFill(int fill) {
		this.fill = fill;
		return this;
	}

	// weightx ָ����ηֲ������ˮƽ�ռ䡣
	// weighty ָ����ηֲ�����Ĵ�ֱ�ռ䡣
	public GBC setWeight(double weightx, double weighty) {
		this.weightx = weightx;
		this.weighty = weighty;
		return this;
	}

	// insets ���ֶ�ָ��������ⲿ��䣬�����������ʾ�����Ե֮�������С����
	// Insets �����������߽�ı�ʾ��ʽ
	// Insets(int top, int left, int bottom, int right)
	// ��������ʼ������ָ����������ߡ��ײ����ұ� inset ���� Insets ����
	public GBC setInsets(int distance) {
		this.insets = new Insets(distance, distance, distance, distance);
		return this;
	}

	// Insets(int top, int left, int bottom, int right)
	// ��������ʼ������ָ����������ߡ��ײ����ұ� inset ���� Insets ����
	public GBC setInsets(int top, int left, int bottom, int right) {
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}

	// ipadx
	// ���ֶ�ָ��������ڲ���䣬�����������С�����Ӷ��Ŀռ䡣
	// int ipady
	// ���ֶ�ָ���ڲ���䣬�����������С�߶���Ӷ��Ŀռ䡣
	public GBC setIpad(int ipadx, int ipady) {
		this.ipadx = ipadx;
		this.ipady = ipady;
		return this;
	}
}
