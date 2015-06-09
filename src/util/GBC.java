package util;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {
	private static final long serialVersionUID = 1L;

	// gridx 指定包含组件的显示区域开始边的单元格，其中行的第一个单元格为 gridx=0。
	// gridy 指定位于组件显示区域的顶部的单元格，其中最上边的单元格为 gridy=0。
	public GBC(int gridx, int gridy) {
		this.gridx = gridx;
		this.gridy = gridy;
	}

	// gridwidth  指定组件显示区域的某一行中的单元格数。
	// gridheight 指定在组件显示区域的一列中的单元格数。
	public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
	}

	// anchor 当组件小于其显示区域时使用此字段。
	public GBC setAnchor(int anchor) {
		this.anchor = anchor;
		return this;
	}

	// fill 当组件的显示区域大于它所请求的显示区域的大小时使用此字段。
	public GBC setFill(int fill) {
		this.fill = fill;
		return this;
	}

	// weightx 指定如何分布额外的水平空间。
	// weighty 指定如何分布额外的垂直空间。
	public GBC setWeight(double weightx, double weighty) {
		this.weightx = weightx;
		this.weighty = weighty;
		return this;
	}

	// insets 此字段指定组件的外部填充，即组件与其显示区域边缘之间间距的最小量。
	// Insets 对象是容器边界的表示形式
	// Insets(int top, int left, int bottom, int right)
	// 创建并初始化具有指定顶部、左边、底部、右边 inset 的新 Insets 对象。
	public GBC setInsets(int distance) {
		this.insets = new Insets(distance, distance, distance, distance);
		return this;
	}

	// Insets(int top, int left, int bottom, int right)
	// 创建并初始化具有指定顶部、左边、底部、右边 inset 的新 Insets 对象。
	public GBC setInsets(int top, int left, int bottom, int right) {
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}

	// ipadx
	// 此字段指定组件的内部填充，即给组件的最小宽度添加多大的空间。
	// int ipady
	// 此字段指定内部填充，即给组件的最小高度添加多大的空间。
	public GBC setIpad(int ipadx, int ipady) {
		this.ipadx = ipadx;
		this.ipady = ipady;
		return this;
	}
}
