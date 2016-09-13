package test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import org.junit.Test;

public class JFreeChartTest {

	@Test
	public void makePngWithPointsStatisticsModel() {
		// 建立JFreeChart
		CategoryDataset dataSet = getDataSet();

		// 如果把createLineChart改为createLineChart3D就变为了3D效果的折线图
		JFreeChart jfc = ChartFactory.createLineChart(null, "时间", "点数据值",
				dataSet, PlotOrientation.VERTICAL, // 绘制方向
				false, // 显示图例
				true, // 采用标准生成器
				false // 是否生成超链接
				);

//		jfc.getLegend().setItemFont(new Font("微软雅黑", 0, 8));
		// 设置标题并且设置其字体，防止中文乱码
		TextTitle textTitle = new TextTitle("测试图表");
		textTitle.setFont(new Font("宋体", Font.BOLD, 10));
		textTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
		jfc.setTitle(textTitle);
		// 设置图表子标题
		TextTitle subTitleText = new TextTitle("子标题");
		subTitleText.setFont(new Font("宋体", Font.BOLD, 10));
		subTitleText.setHorizontalAlignment(HorizontalAlignment.LEFT);
		jfc.addSubtitle(subTitleText);
		CategoryPlot plot = (CategoryPlot) jfc.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
//		render.setBaseShapesVisible(true);
//		render.setBaseShapesFilled(true);
//		render.setSeriesOutlineStroke(0, new BasicStroke(0.3F));// 设置折点的大小

		// x轴
		CategoryAxis xAxis = plot.getDomainAxis();
		Font font = new Font("courier new", Font.BOLD, 8);
		Font font2 = new Font("微软雅黑", Font.BOLD, 10);
		xAxis.setTickLabelFont(font);
		xAxis.setLabelFont(font2);
		xAxis.setVisible(false);
		// y轴
		ValueAxis yAxis = plot.getRangeAxis();
		// yAxis.setRange(min - (max - min) / 2,
		// max + (max - min) / 2);
		yAxis.setTickLabelFont(font);
		yAxis.setLabelFont(font2);
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File("D:/test.png"));
			ChartUtilities.writeChartAsPNG(out, jfc, 500, 240);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成测试数据
	 * 
	 * @return
	 */
	private CategoryDataset getDataSet() {
//		PointStatisticsModel []models = {
//				 new PointStatisticsModel("12:31",105.23),
//				 new PointStatisticsModel("12:32",75.23),
//				 new PointStatisticsModel("12:33",88.63),
//				 new PointStatisticsModel("12:34",99.23),
//				 new PointStatisticsModel("12:35",34.53),
//				 new PointStatisticsModel("12:36",66.8),
//				 new PointStatisticsModel("12:37",70.23),
//				 new PointStatisticsModel("12:38",23.23),
//				 new PointStatisticsModel("12:39",200.73),
//				 new PointStatisticsModel("12:40",150.53),
//				 new PointStatisticsModel("12:41",85.15),
//				 new PointStatisticsModel("12:42",140.56),
//				 new PointStatisticsModel("12:43",145.43),
//				 new PointStatisticsModel("12:44",210.15),
//				 new PointStatisticsModel("12:45",143.15)};
//		
//		DefaultKeyedValues kvs = new DefaultKeyedValues();
//		int i=0;
//		for(PointStatisticsModel model:models){
//			i++;
//			kvs.addValue(""+i, model.getyAxis());
//		}
//		for(PointStatisticsModel model:models){
//			i++;
//			kvs.addValue(""+i, model.getyAxis());
//		}
//		for(PointStatisticsModel model:models){
//			i++;
//			kvs.addValue(""+i, model.getyAxis());
//		}
//		for(PointStatisticsModel model:models){
//			i++;
//			kvs.addValue(""+i, model.getyAxis());
//		}
//		for(PointStatisticsModel model:models){
//			i++;
//			kvs.addValue(""+i, model.getyAxis());
//		}
//		XYSeries series = new XYSeries("温度");
//		return DatasetUtilities.createCategoryDataset("温度", kvs);
		return null;
	}
}
