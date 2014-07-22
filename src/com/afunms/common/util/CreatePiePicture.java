package com.afunms.common.util;

import ChartDirector.Chart;
import ChartDirector.PieChart;
import ChartDirector.TextBox;

public class CreatePiePicture {

	/**
	 * @author lgw
	 * @date Mar 1, 2011 4:56:57 PM
	 * @param args
	 *            void
	 * @Description: TODO(�����������������)
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CreatePiePicture cpp = new CreatePiePicture();
		// double[] depth = {5,7,9,11,13,15};
		double[] data = { 25, 18 };
		String[] labels = { "�й�", "�ձ�" };
		int[] colors = { 0xb8bc9c, 0xecf0b9 };
		TitleModel titleModel = new TitleModel();
		titleModel.setXpic(140);
		titleModel.setYpic(120);
		titleModel.setX1(70);
		titleModel.setX2(40);
		titleModel.setX3(45);
		titleModel.setBgcolor(0xffffff);
		titleModel.setPictype("png");
		titleModel.setTopTitle("�й��й��й�");
		cpp.createPieChartWithLegend(labels, colors, data, titleModel);
	}
	/**
	 * 
	 * @author wxy
	 * @date Feb 25, 2011 4:56:35 PM
	 * @param label
	 * @param data
	 * @param titleModel
	 * @return void
	 * @Description: TODO(PieChartWithLegend)-��ǰ��ͨ�ʱ�ͼ
	 * 
	 */
	public void createPingPic(String ip, double realValue) {
		CreatePiePicture _cpp = new CreatePiePicture();
		TitleModel _titleModel = new TitleModel();
		_titleModel.setXpic(150);
		_titleModel.setYpic(150);// 160, 200, 150, 100
		_titleModel.setX1(75);// �⻷�����λ��
		_titleModel.setX2(60);// �⻷���ϵ�λ��
		_titleModel.setX3(65);
		_titleModel.setX4(30);
		_titleModel.setX5(75);
		_titleModel.setX6(70);
		_titleModel.setX7(10);
		_titleModel.setX8(115);
		_titleModel.setBgcolor(0xffffff);
		_titleModel.setPictype("png");
		_titleModel.setPicName(ip + "realping");
		_titleModel.setTopTitle("");

		double[] _data1 = { realValue, 100 - realValue };
		String[] p_labels = { "��ͨ", "δ��ͨ" };
		int[] _colors = { 0x66ff66, 0xff0000 };
		_cpp.createOneRingChart(_data1, p_labels, _colors, _titleModel);
	}
	/**
	 * 
	 * @author wxy
	 * @date Feb 25, 2011 4:56:35 PM
	 * @param label
	 * @param data
	 * @param titleModel
	 * @return void
	 * @Description: TODO(PieChartWithLegend)-��С��ͨ�ʱ�ͼ
	 * 
	 */
	public void createMinPingPic(String ip, String maxpingvalue) {
		TitleModel ping_titleModel = new TitleModel();
        ping_titleModel.setXpic(150);
        ping_titleModel.setYpic(150);//160, 200, 150, 100
        ping_titleModel.setX1(75);//�⻷�����λ��
        ping_titleModel.setX2(60);//�⻷���ϵ�λ��
        ping_titleModel.setX3(65);
        ping_titleModel.setX4(30);
        ping_titleModel.setX5(75);
        ping_titleModel.setX6(70);
        ping_titleModel.setX7(10);
        ping_titleModel.setX8(115);
        ping_titleModel.setBgcolor(0xffffff);
        ping_titleModel.setPictype("png");
        ping_titleModel.setPicName(ip+"minping");
        ping_titleModel.setTopTitle("");
        double d_maxping = new Double(maxpingvalue);
        double[] minping_data1 = {d_maxping, 100-d_maxping};
        String[] ping_labels = {"��ͨ", "δ��ͨ"};
        int[] ping_colors = {0x66ff66, 0xff0000}; 
        createOneRingChart(minping_data1,ping_labels,ping_colors,ping_titleModel);
	}
	/**
	 * 
	 * @author wxy
	 * @date Feb 25, 2011 4:56:35 PM
	 * @param label
	 * @param data
	 * @param titleModel
	 * @return void
	 * @Description: TODO(PieChartWithLegend)-���ɵ���ƽ����ͨ��ͼ�α�ͼ
	 * 
	 */
	public void createAvgPingPic(String ip, double avgpingcon) {
		
	        TitleModel _titleModel = new TitleModel();
	        _titleModel.setXpic(150);
	        _titleModel.setYpic(150);//160, 200, 150, 100
	        _titleModel.setX1(75);//�⻷�����λ��
	        _titleModel.setX2(60);//�⻷���ϵ�λ��
	        _titleModel.setX3(65);
	        _titleModel.setX4(30);
	        _titleModel.setX5(75);
	        _titleModel.setX6(70);
	        _titleModel.setX7(10);
	        _titleModel.setX8(115);
	        _titleModel.setBgcolor(0xffffff);
	        _titleModel.setPictype("png");
	        _titleModel.setPicName(ip+"pingavg");
	        _titleModel.setTopTitle("");
	        
	        double[] _data1 = {avgpingcon, 100-avgpingcon};
	        String[] p_labels = {"��ͨ", "δ��ͨ"};
	        int[] _colors = {0x66ff66, 0xff0000}; 
	       createOneRingChart(_data1,p_labels,_colors,_titleModel);
	}
	/**
	 * 
	 * @author lgw
	 * @date Feb 25, 2011 4:56:35 PM
	 * @param label
	 * @param data
	 * @param titleModel
	 * @return String
	 * @Description: TODO(PieChartWithLegend)
	 * 
	 */
	public String createPieChartWithLegend(String[] labels, int[] colors,
			double[] data, TitleModel tm) {
		Chart.setLicenseCode(CommonMethod.keycode);

		PieChart c = new PieChart(tm.getXpic(), tm.getYpic());
		c.setPieSize(tm.getX1(), tm.getX2(), tm.getX3());
		c.set3D();
		c.setBackground(tm.getBgcolor());
		c.addLegend(2, 78, false, "����", 9).setBackground(Chart.Transparent);
		c.setColor(Chart.LineColor, 0xc0c0c0);
		c.setColors2(Chart.DataColor, colors);
		c.setLabelFormat("{percent|0}%");
		c.setData(data, labels);
		c.setLabelStyle("����");
		String picname = tm.getPicName() + ".png";
		String str = CommonMethod.checkFile() + "/" + picname;
		c.makeChart(str);
		return picname;
	}

	/**
	 * 
	 * @author lgw
	 * @date Mar 4, 2011 11:44:09 AM
	 * @param data1
	 * @param title1
	 * @param labels
	 * @param colors
	 * @return String
	 * @Description: TODO(������״ͼ)
	 */
	public String createOneRingChart(double[] data1, String[] labels,
			int[] colors, TitleModel tm) {
		Chart.setLicenseCode(CommonMethod.keycode);
		PieChart c = new PieChart(tm.getXpic(), tm.getYpic());
		c.setBackground(tm.getBgcolor());
		c.addTitle(tm.getTopTitle(), "����", 18);
		c.setDonutSize(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4());
		TextBox t = c.addText(tm.getX5(), tm.getX6(), tm.getTopTitle(), "����",
				12, 0x000000);
		t.setBackground(tm.getBgcolor(), Chart.Transparent, Chart
				.softLighting());
		t.setRoundedCorners(5);
		c.addLegend(tm.getX7(), tm.getX8(), false, "����", 9).setBackground(
				Chart.Transparent, Chart.Transparent);
		c.setData(data1, labels);
		c.setColors2(Chart.DataColor, colors);
		c.setLineColor(0xffffff);
		c.setLabelFormat("{percent|0}%");
		c.setLabelStyle("����", 10);
		c.setLabelPos(-25);
		c.set3D();
		String picname = tm.getPicName() + ".png";
		String str = CommonMethod.checkFile() + "/" + picname;
		c.makeChart(str);
		return picname;
	}

	/**
	 * 
	 * @author lgw
	 * @date Mar 4, 2011 10:07:25 AM
	 * @param data1
	 * @param title1
	 * @param data2
	 * @param title2
	 * @param labels
	 * @param colors
	 * @return String
	 * @Description: TODO(˫���Ƚ�ͼ)
	 */
	public String createTwoConcentricDonutChart(double[] data1, String title1,
			double[] data2, String title2, String[] labels, int[] colors,
			TitleModel tm) {
		Chart.setLicenseCode(CommonMethod.keycode);
		PieChart c = new PieChart(tm.getXpic(), tm.getYpic());
		c.addTitle(tm.getTopTitle(), "����", 18);
		c.setDonutSize(tm.getX1(), tm.getX2(), tm.getX3(), tm.getX4());
		TextBox t = c
				.addText(tm.getX5(), tm.getX6(), title1, "����", 8, 0xffffff);
		t.setBackground(0x008800, Chart.Transparent, Chart.softLighting());
		t.setRoundedCorners(5);
		c.addLegend(tm.getX7(), tm.getX8(), false, "����", 8).setBackground(
				Chart.Transparent, Chart.Transparent);
		c.setData(data1, labels);
		c.setColors2(Chart.DataColor, colors);
		c.setLineColor(0xffffff);
		c.setLabelFormat("{percent|0}%");
		c.setLabelStyle("����", 10);
		c.setLabelPos(-25);
		c.set3D();
		PieChart c2 = new PieChart(tm.getR2x(), tm.getR2y(), Chart.Transparent);
		c2.setDonutSize(tm.getR2x1(), tm.getR2x2(), tm.getR2x3(), tm.getR2x4());
		TextBox t2 = c2.addText(tm.getR2textx(), tm.getR2texty(), title2, "����",
				8, 0xffffff, Chart.Center);
		t2.setBackground(0x0000cc, Chart.Transparent, Chart.softLighting());
		t2.setRoundedCorners(5);
		c2.setData(data2, labels);
		c2.setColors2(Chart.DataColor, colors);
		c2.setLineColor(0xffffff);
		c2.setLabelFormat("{percent|0}%");
		c2.setLabelStyle("����", 10);
		c2.setLabelPos(-25);
		c2.set3D();
		c.makeChart3().merge(c2.makeChart3(), tm.getInr2x(), tm.getInr2y(),
				Chart.TopLeft, 0);
		String picname = tm.getPicName() + ".png";
		String str = CommonMethod.checkFile() + "/" + picname;
		c.makeChart(str);
		return picname;
	}
}
