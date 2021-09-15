package application.model.game.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class MatchChart extends BarChart<String, Number>{

	private static final String TITLE = "Game chart of" ;
	
	private Axis<String> xAxis = null ;
	private Axis<Number> yAxis = null ;
	private List<DataMatch> dataMatches = null ;
	private String aboutWho = "";
	private XYChart.Series<String, Number> matchesWon = null ;
	private XYChart.Series<String, Number> matchesLost = null ;
	
	public MatchChart(String aboutWho , Axis<String> xAxis, Axis<Number> yAxis,List<DataMatch> dataMatches) {
		super(xAxis, yAxis);
		this.xAxis = xAxis ;
		this.yAxis = yAxis ;
		this.aboutWho = aboutWho ;
		this.dataMatches = dataMatches ;
		this.setTitle(TITLE+" "+aboutWho);
		xAxis.setLabel("Date");
		yAxis.setLabel("Match");
		matchesWon = new XYChart.Series<String,Number>();
		matchesLost = new XYChart.Series<String,Number>();
		matchesWon.setName("Matches won");
		matchesLost.setName("Matches lost");
		initialize();
	}
	
	
	private void initialize() {
		
		Set<String> days = new TreeSet<>();
		
		for(DataMatch d : dataMatches)
			days.add(d.getDate());
		
		for(String day : days) {
			int won = 0 ;
			int lost = 0 ;
			for(DataMatch d : dataMatches) {
				if(d.getDate().equals(day)) {
					if(d.getHome().equals(aboutWho) && d.whoWon() == DataMatch.HOME)
						++won ;
					else if(d.getGuest().equals(aboutWho) && d.whoWon() == DataMatch.GUEST)
						++won ;
					else 
						++lost ;
				}
			}
			matchesWon.getData().add(new XYChart.Data<String, Number>(day, won));
			matchesLost.getData().add(new XYChart.Data<String, Number>(day, lost));
		}
		
		this.getData().add(matchesWon);
		this.getData().add(matchesLost);
		
	}
	
	
	

}
