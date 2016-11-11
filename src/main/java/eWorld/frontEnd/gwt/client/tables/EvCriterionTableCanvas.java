package eWorld.frontEnd.gwt.client.tables;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import eWorld.datatypes.containers.IdCriterionValueContainer;
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.data.IdCriterionValue;
import eWorld.datatypes.elementars.IntegerVote;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvStyle;

/**
 * Provides a Canvas based interface to for evaluating CriterionValues,
 * it should already be ensured that canvas is supported
 * @author michael
 *
 */
public class EvCriterionTableCanvas extends EvCriterionTable {

	private static final int MIN_E_BAR_WIDTH = 200;
	
	
	// attributes
	
	private final int colIndexEvaluatebar = getColIndexExtensionStartCriterionTable();
	
	/** stores the width that is set to EvBars */
	private int eBarWidth = -1;
	
	// constructors
	
	public EvCriterionTableCanvas(boolean eColsVisible) {
		super(eColsVisible);

		// style
		this.addStyleName(EvStyle.eCriterionTableCanvas);
		this.getColumnFormatter().addStyleName(colIndexName, EvStyle.eCriterionTableCanvasNameColumn);
		this.getColumnFormatter().addStyleName(colIndexEvaluatebar, EvStyle.eCriterionTableCanvasEvBarColumn);
	}
	
	
	// methods
	
	@Override
	protected void extendCaptionRowCriterion(int row) {
		// nothing
	}

	@Override
	protected void extendDataRowCriterion(int row,
			EvCriterion<Ev, CriterionShortIdentifier> criterion) {
		
		final EvBar eBar = new EvBar(criterion);
		eBar.render(getEvBarWidth());
		eBar.addDomHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				assert null != event;
				
				IdCriterionValue<CriterionShortIdentifier> criterionValue = eBar.getAverageCriterionValueFromPosition(event.getRelativeX(eBar.getElement()), event.getRelativeY(eBar.getElement()));
				if (null == criterionValue) {
					eBar.popup.resetAndHide();
				} else {
					eBar.popup.setValueAndShow(criterionValue, 
							eBar.getHorizontalPositionOfAverage(criterionValue), 
							eBar.getVerticalPositionOfAverage());
				}
			}
		}, MouseMoveEvent.getType());
		
		setWidget(row, colIndexEvaluatebar, eBar);
		
		// style
		eBar.setStyleName(EvStyle.eCriterionTableCanvasEvBar);
		getCellFormatter().addStyleName(row, colIndexEvaluatebar, EvStyle.eCriterionTableCanvasEvBarCell);
		if (isEvaluable()) {
			getCellFormatter().addStyleName(row, colIndexEvaluatebar, EvStyle.eCriterionTableCanvasEvBarCellEvaluable);
		}
	}
	
	/**
	 * TODO bad implementation
	 * @return
	 */
	private int getEvBarWidth() {
		if (MIN_E_BAR_WIDTH > eBarWidth) {
			eBarWidth = Math.max(MIN_E_BAR_WIDTH, getColumnFormatter().getElement(colIndexEvaluatebar).getOffsetWidth());
		}
		return eBarWidth;
	}

	@Override
	protected void setCriterionValueCaption(int row) {
		
		final EvBar eBarHeader = new EvBar(true);
		eBarHeader.render(getEvBarWidth());
		setWidget(row, colIndexEvaluatebar, eBarHeader);
		
		// style
		eBarHeader.setStyleName(EvStyle.eCriterionTableCanvasEvBar);
		getCellFormatter().addStyleName(row, colIndexEvaluatebar, EvStyle.eCriterionTableCanvasEvBarCell);
	}

	@Override
	public void displayCriterionValueContainer() {
		assert null != getValueItemName();
		assert null != getValueContainer();
		
		// caption
		setCriterionValueCaption(0);
		
		ArrayList<EvCriterion<Ev, CriterionShortIdentifier>> criteria = getItemContainer().getData();
		ArrayList<IdCriterionValue<CriterionShortIdentifier>> criterionValues = getValueContainer().getData();
		
		int rowAverageLast = -1;
		IdCriterionValue<CriterionShortIdentifier> valueAverageLast = null;
		EvCriterion<Ev, CriterionShortIdentifier> criterionAverageLast = null;
		int rowAverageNow = -1;
		IdCriterionValue<CriterionShortIdentifier> valueAverageNow= null;
		EvCriterion<Ev, CriterionShortIdentifier> criterionAverageNow = null;
		
		int searchIndex = 0;
		for (int i = 0; i < criteria.size(); i++) {
			EvCriterion<Ev, CriterionShortIdentifier> criterion = criteria.get(i);
			
			int foundIndex = findMatching(criterion, searchIndex, criterionValues);
			int row = i +1;
			
			if (-1 < foundIndex) {
				// value does exist
				
				setCriterionValueIndividual(row, criterionValues.get(foundIndex));
				
				rowAverageLast = rowAverageNow;
				valueAverageLast = valueAverageNow;
				criterionAverageLast = criterionAverageNow;
				rowAverageNow = row;
				valueAverageNow = criterionValues.get(foundIndex);
				criterionAverageNow = criterion;
				
				setCriterionValueAverage(rowAverageLast, valueAverageLast, criterionAverageLast, rowAverageNow, valueAverageNow, criterionAverageNow);
				
				searchIndex = foundIndex +1;
			} else {
				// value does not yet exist
				setCriterionValueIndividual(row, null);
			}
			
			// add style to cell
			if (isEvaluable()) {
				getCellFormatter().addStyleName(row, colIndexEvaluatebar, EvStyle.eCriterionTableCanvasEvBarCellEvaluable);
			} else {
				getCellFormatter().removeStyleName(row, colIndexEvaluatebar, EvStyle.eCriterionTableCanvasEvBarCellEvaluable);
			}
		}
	}
	/**
	 * @param row the index of the row where the given criterionValue should be set
	 * @param criterionValue null when criterionValue is not(yet) available
	 */
	private void setCriterionValueIndividual(int row,
			IdCriterionValue<CriterionShortIdentifier> criterionValue) {
		
		EvBar evaluateBar = (EvBar)getWidget(row, colIndexEvaluatebar);
		assert null != evaluateBar;
		
		if (null != criterionValue) {
			evaluateBar.setCriterionValue(criterionValue.getIndividualVote());
		} else {
			evaluateBar.setCriterionValue(null);
		}
	}
	/**
	 * 
	 * @param rowLast arbitrary when null == valueLast
	 * @param valueLast null when value before valueNow does not exist
	 * @param rowNow
	 * @param valueNow
	 */
	private void setCriterionValueAverage(
			int rowLast, IdCriterionValue<CriterionShortIdentifier> valueLast, EvCriterion<Ev, CriterionShortIdentifier> criterionLast,
			int rowNow, IdCriterionValue<CriterionShortIdentifier> valueNow, EvCriterion<Ev, CriterionShortIdentifier> criterionNow) {
		assert -1 < rowNow;
		assert null != valueNow;
		
		// set connections of rows where criterionValue is missing
		// TODO TODO TODO
		
		// set averagePoint of valueNow
		EvBar evaluateBarNow = (EvBar)getWidget(rowNow, colIndexEvaluatebar);
		assert null != evaluateBarNow;
		evaluateBarNow.setAverageElement(criterionNow, valueNow, rowNow, "#000080");
		evaluateBarNow.setValueAveragePoint(valueNow);
		
		// draw connection in EvBar of valueLast
		if (0 > rowLast) {
			rowLast = 0;
		}
		EvBar evaluateBarLast = (EvBar)getWidget(rowLast, colIndexEvaluatebar);
		assert null != evaluateBarLast;
		if (null == valueLast) {
			// indicates, that valueNow is first value in table -> set header
			criterionLast = criterionNow;
			valueLast = valueNow;
			evaluateBarLast.setAverageElement(criterionLast, valueLast, rowLast, "#000080");
			evaluateBarLast.setHeaderInscription();
		}
		evaluateBarLast.setValueAverageConnectNext(criterionNow, valueNow, rowNow);
		
		// draw connection in EvBar of valueNow
		evaluateBarNow.setValueAverageConnectLast(criterionLast, valueLast, rowLast);
	
	}

	@Override
	protected void rowSelectedCriterionTable(
			EvCriterion<Ev, CriterionShortIdentifier> item, Cell cell, ClickEvent event) {
		
		if (cell.getCellIndex() == colIndexEvaluatebar &&
				true == isEvaluable() &&
				null != getValueContainer()) {
			
			final EvBar evaluateBar = (EvBar)this.getWidget(cell.getRowIndex(), colIndexEvaluatebar);
			final int value = evaluateBar.getValueFromPosition(event.getRelativeX(evaluateBar.getElement()), event.getRelativeY(evaluateBar.getElement()));
			
			EvApp.REQ.voteCriterionValue(new CriterionIdentifier(getValueContainer().getClassIdentifier(), item.getIdentifier()), value, new EvAsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					evaluateBar.setValue(value);
				}
				@Override
				public void onFailure(Throwable caught) {
					super.onFailure(caught);
					evaluateBar.setNotSignedInNote();
				}
			});
			
			evaluateBar.setEvaluatingNote();
		}
		
	}

	private class EvBar extends FlowPanel {
		
		// (static finals) (configuration)
		
		private static final int marginLeft = 25;
		private static final int marginRight = 25;
		private static final int marginTop = 3;
		private static final int marginBottom = 5;
		
		private static final int eBarMarginTop = 7;
		private int eBarWidth;	// set by render(..)
		private static final int eBarHeight = 30;	// also used in code
		private static final int heightHeader = 30;	// there are no additional borders
		private static final int eBarHeaderInscriptionY = 20;
		
		private static final int inscrMarginTop = 2;
		private static final int inscrHeight = 8;
		
		
		// (static finals) (used in code)
		
		private static final int eBarStartX = marginLeft;
		private int eBarEndX;	// set by render(..)
		private static final int eBarStartY = marginTop + eBarMarginTop;
		private static final int eBarEndY = eBarStartY + eBarHeight;
		
		private static final int inscrStartY = eBarEndY + inscrMarginTop;
		private static final int inscrEndY = inscrStartY + inscrHeight;
		
//		private static final int width = eBarEndX + marginRight;
		private int width;	// set by render(..)
		private static final int height = inscrEndY + marginBottom;
		
		private static final int minValueBlockWidth = 10;
		private static final int valueBlockMargin = 1;
		
		private static final int colorWorst = 200;	// red
		private static final int colorBest = 150;	// green
		private static final String colorEvBarBackground = "#DBDBDB";
		private static final String colorEvBarBackgroundBar = "#D0D0D0";
		private static final String colorIndividual = "#0030C0";
		private static final String colorEvaluationBar = "#F0C000";
		
		private static final int averagePointWidth = 4;	// %2 should be 0
		private static final int averagePointHeight = 4;	// %2 should be 0
		private static final int averageLineWidth = 2;
		private static final int averageSurroundingWidth = 8;	// %2 should be 0
		private static final int averageSurroundingHeight = 8;	// %2 should be 0
		private static final int averageHoveringWidth = 14;	// width of the area where mouse can be moved to display details, %2 should be 0
		private static final int averageHoveringHeight = 14;	// height of the area where mouse can be moved to display details, %2 should be 0
		private static final int averageLessEvs = 5;
		private static final int averageFewEvs = 15;
		private static final int averageSomeEvs = 50;
		private static final String averageColorLessEvs = "#A00";
		private static final String averageColorFewEvs = "#F80";
		private static final String averageColorSomeEvs = "#0C0";
		private static final String averageColorManyEvs = "#DEF";
		
		private static final String standardFont = "normal 13px Arial Unicode MS,Arial,sans-serif";
		
		
		// attributes
		
		private EvCriterion<Ev,CriterionShortIdentifier> criterion;
		private boolean headerOnly = false;
		private IntegerVote individualVote = null;
		private EvBarAverageElement averageElement = new EvBarAverageElement();
		
		private Context2d c;
		
		// components
		
		private CriterionValueViewPopup popup = new CriterionValueViewPopup();
		private Canvas canvas;
		
		// constructors
		
		/**
		 * the method render(..) must be called manually to display the widget
		 * @param criterion
		 * @param headerOnly true when this instance should only be a legend or explanation (in most cases row 0 of the table), notice: most functions shouldn't be used then
		 */
		public EvBar(EvCriterion<Ev,CriterionShortIdentifier> criterion) {
			assert null != criterion;
			
			init(criterion, false);
		}
		
		/**
		 * use this constructor just if instance should only be a legend or explanation (in most cases row 0 of the table), 
		 * notice: most functions shouldn't be used then
		 * @param headerOnly true, this 
		 */
		public EvBar(boolean headerOnly) {
			assert true == headerOnly;
			
			init(null, headerOnly);
		}
		
		private void init(EvCriterion<Ev,CriterionShortIdentifier> criterion, boolean headerOnly) {
			
			this.criterion = criterion;
			this.headerOnly = headerOnly;
			
			canvas = Canvas.createIfSupported();
			if (!headerOnly) {
				canvas.setCoordinateSpaceHeight(height);
			} else {
				canvas.setCoordinateSpaceHeight(heightHeader);
			}
			c = canvas.getContext2d();
			
			add(popup);
			add(canvas);
		}
		
		
		// general methods
		
		private void setWidth(int width) {
			this.width = width;
			eBarWidth = width - marginLeft - marginRight;
			eBarEndX = eBarStartX + eBarWidth;
			
			canvas.setCoordinateSpaceWidth(width);
		}
		
		/**
		 * renders the widget in the given width
		 * must be manually called
		 * @param width
		 */
		public void render(int width) {
			setWidth(width);
			if (!headerOnly) {
				resetAll();
			}
		}
		
		public void setAverageElement(EvCriterion<Ev,CriterionShortIdentifier> criterion, IdCriterionValue<CriterionShortIdentifier> value, int row, String lineStyle) {
			assert null != criterion;
			
			this.criterion = criterion;
			
			averageElement.set(criterion, value, row);
			averageElement.setStyle(lineStyle);
		}
		
		private int getWorst() {
			return criterion.getWorst();
		}
		
		private int getBest() {
			return criterion.getBest();
		}
		
		private void resetAll() {
			reset();
			setCriterionValue(individualVote);
		}
		
		private void reset() {
			c.clearRect(0, 0, width, height);
			resetEvBar();
			setInscription();
		}
		
		private void resetEvBar() {
//			c.setFillStyle(colorEvBarBackground);
//			c.fillRect(eBarStartX, eBarStartY, eBarWidth, eBarHeight);
			setBar(getBest(), colorEvBarBackgroundBar);
		}
		
		private void setInscription() {
			setInscriptionCipher(0);
			setInscriptionCipher(0.25f);
			setInscriptionCipher(0.5f);
			setInscriptionCipher(0.75f);
			setInscriptionCipher(1);
		}
		
		/**
		 * @param position 0 means left, 1 means right, 0.5 means center, etc.
		 */
		private void setInscriptionCipher(float position) {
			assert 0 <= position;
			assert 1 >= position;
			
			int worst = getWorst();
			int best = getBest();
			
			c.setFillStyle("rgb(" + (int)(colorWorst * (1f-position)) + ", " + (int)(colorBest * position) + ", 0)");
			c.setFont(standardFont);
			c.setTextAlign(Context2d.TextAlign.CENTER);
			c.setTextBaseline(Context2d.TextBaseline.ALPHABETIC);
			c.fillText(String.valueOf((best-worst) * position + worst), eBarStartX + eBarWidth*position, inscrEndY);
//			c.fillText(NumberFormat.getFormat("0.00").format((best-worst) * position + worst), eBarStartX + eBarWidth*position, inscrEndY);
		}
		
		/**
		 * calculates what width|positionX of the eBar is represented by the given value
		 * @param value
		 * @return the eBar-length|eBarPosistionX that represents the given value
		 */
		private int valueToWidth(float value, int worst, int best) {
			return (int)((eBarWidth * (value-worst)) / (best-worst));
		}
		
		
		// methods for individual evaluates
		
		private void setValue(int value) {
			reset();
			
			setBar(value, colorEvaluationBar);
			
			c.setFillStyle(colorIndividual);
			c.setFont(standardFont);
			c.setTextBaseline(Context2d.TextBaseline.MIDDLE);
			c.setTextAlign(Context2d.TextAlign.CENTER);
			c.fillText(String.valueOf(value), eBarStartX + valueToWidth(value, getWorst(), getBest()), height/2);
			
			setAverage();
		}
		/**
		 * draws a bar from getWorst() to the given value
		 * @param value
		 * @param style style|color the bar should have
		 */
		private void setBar(int value, String style) {
			int worst = getWorst();
			int best = getBest();
			
			assert (value >= worst && value <= best) || (value <= worst && value >= best);
			int valueBlockWidth = eBarWidth / Math.abs(best-worst);
			c.setFillStyle(style);
			if (minValueBlockWidth <= valueBlockWidth) {
				
				if (worst <= value) {
					for (int i = worst; i <= value; i++) {
						setValueBlock(i, valueBlockWidth);
					}
				} else {
					for (int i = worst; i >= value; i--) {
						setValueBlock(i, valueBlockWidth);
					}
				}
				
			} else {
				int rectWidth = valueToWidth(value, worst, best) + (eBarWidth / (best-worst)) / 2;
				if (eBarWidth < rectWidth) {
					rectWidth = eBarWidth;
				}
				c.fillRect(eBarStartX, eBarStartY, rectWidth, eBarHeight);
			}
		}
		private void setValueBlock(int value, int valueBlockWidth) {
			
			int worst = getWorst();
			int best = getBest();
			
			int startX = eBarStartX + valueToWidth(value, worst, best);
			int rectWidth = valueBlockWidth - valueBlockMargin*2;
			if (value != worst) {
				startX -= rectWidth/2;
			}
			if (value == worst || value == best) {
				rectWidth /= 2;
			}
			c.fillRect(startX, eBarStartY, rectWidth, eBarHeight);
		}
		
		private void setNotEvaluatedNote() {
			reset();
			
			c.setFillStyle(colorIndividual);
			c.setFont(standardFont);
			c.setTextAlign(Context2d.TextAlign.CENTER);
			c.setTextBaseline(Context2d.TextBaseline.MIDDLE);
//			c.fillText("You have not evaluated.", eBarStartX + eBarWidth/2, eBarStartY + eBarHeight/2);
			c.fillText("Click to evaluate.", eBarStartX + eBarWidth/2, eBarStartY + eBarHeight/2);
			
			setAverage();
		}
		
		public void setEvaluatingNote() {
			reset();
			
			c.setFillStyle(colorIndividual);
			c.setFont(standardFont);
			c.setTextAlign(Context2d.TextAlign.CENTER);
			c.setTextBaseline(Context2d.TextBaseline.MIDDLE);
			c.fillText("Evaluating...", eBarStartX + eBarWidth/2, eBarStartY + eBarHeight/2);
			
			setAverage();
		}
		
		public void setNotSignedInNote() {
			reset();
			
			c.setFillStyle(colorIndividual);
			c.setFont(standardFont);
			c.setTextAlign(Context2d.TextAlign.CENTER);
			c.setTextBaseline(Context2d.TextBaseline.MIDDLE);
			c.fillText("You have to sign in to evaluate.", eBarStartX + eBarWidth/2, eBarStartY + eBarHeight/2);
			
			setAverage();
		}
		
		public void setCriterionValue(IntegerVote individualVote) {
			this.individualVote = individualVote;
			
			if (null != individualVote) {
				if (null != individualVote.getVote()) {
					setValue(individualVote.getVote());
				} else {
					setNotEvaluatedNote();
				}
			} else if (isEvaluable()) {
				setNotEvaluatedNote();
			}
			
		}
		
		public int getValueFromPosition(int posX, int posY) {
			assert 0 <= posX;
			assert 0 <= posY;
			assert width >= posX;
			assert height >= posY;
			
			int worst = getWorst();
			int best = getBest();
			
			int value = (int)(((float)(posX-eBarStartX) / eBarWidth) * (best-worst) +0.5*Math.signum(best-worst)) +worst;
			
			int min = worst;
			int max = best;
			if (best < worst) {
				min = best;
				max = worst;
			}
			if (value < min) {
				return min;
			} else if (value > max) {
				return max;
			} else {
				return value;
			}
		}
		
		
		// methods for average evaluates
		
		private void setAverage() {
			if (null != averageElement.getStyle()) {
				if (null != averageElement.getValue()) {
					setValueAveragePoint(averageElement.getValue());
					if (null != averageElement.getValueLast()) {
						setValueAverageConnectLast();
					}
					if (null != averageElement.getValueNext()) {
						setValueAverageConnectNext();
					}
				} else if (null != averageElement.getValueLast() || null != averageElement.getValueNext()) {
					setValueAverageNotExist(
							averageElement.getRowLast(), averageElement.getValueLast(),
							averageElement.getRowNext(), averageElement.getValueNext(),
							averageElement.getStyle());
				}
			}
		}
		
		/**
		 * 
		 * @param rowLast arbitrary when null == valueLast
		 * @param valueLast the last known value, null when not existing (but one of valueLast and valueNext must exist)
		 * @param rowNext arbitrary when null == valueNext
		 * @param valueNext the next known value, null when not existing (but one of valueLast and valueNext must exist)
		 * @param lineStyle the style of the line to draw
		 */
		public void setValueAverageNotExist(
				int rowLast, IdCriterionValue<CriterionShortIdentifier> valueLast,
				int rowNext, IdCriterionValue<CriterionShortIdentifier> valueNext,
				String lineStyle) {
			assert (-1 < rowLast && null != valueLast) || (-1 < rowNext && null != valueNext);
			
			// TODO TODO TODO
		}
		
		/**
		 * sets a rect that represents the average of the given CriterionValue
		 * @param value
		 * @param style the style of the rect
		 */
		public void setValueAveragePoint(IdCriterionValue<CriterionShortIdentifier> value) {
			assert null != value;
			assert null != averageElement.getStyle();
			
			int positionX = eBarStartX + valueToWidth(value.getAverage(), getWorst(), getBest());
			int positionY = eBarStartY + eBarHeight/2;
			
			// draw surrounding
			if (value.getVotes() <= averageLessEvs) {
				c.setFillStyle(averageColorLessEvs);
			} else if (value.getVotes() <= averageFewEvs) {
				c.setFillStyle(averageColorFewEvs);
			} else if (value.getVotes() <= averageSomeEvs) {
				c.setFillStyle(averageColorSomeEvs);
			} else {
				c.setFillStyle(averageColorManyEvs);
			}
			c.fillRect(positionX - averageSurroundingWidth/2, positionY - averageSurroundingHeight/2, averageSurroundingWidth, averageSurroundingHeight);
			
			// draw point
			c.setFillStyle(averageElement.getStyle());
			c.fillRect(positionX - averagePointWidth/2, positionY - averagePointHeight/2, averagePointWidth, averagePointHeight);
		}
		
		/**
		 * draws a line that connects the average of the CriterionValue value to the average of the CriterionValue valueOther
		 * @param row specifies the row where value belongs to
		 * @param value
		 * @param rowOther specifies the row where valueOther belongs to
		 * @param valueOther
		 * @param style the style of the line
		 */
		private void setValueAverageConnect(
				int row, IdCriterionValue<CriterionShortIdentifier> value,
				int rowOther, IdCriterionValue<CriterionShortIdentifier> valueOther, int worstOther, int bestOther,
				String style) {
			assert -1 < row;
			assert null != value;
			assert -1 < rowOther;
			assert null != valueOther;
			assert null != style;
			assert row != rowOther;
			
			int eBarPosX = valueToWidth(value.getAverage(), getWorst(), getBest());
			int eBarOtherPosX = valueToWidth(valueOther.getAverage(), worstOther, bestOther);
			int rowDiff = row - rowOther;
			int xIncrementPerRow = (eBarPosX-eBarOtherPosX) / rowDiff;
			int increment = 1;
			if (0 > rowDiff) {
				increment = -1;
			}
			
			int eBarPosY = eBarStartY + eBarHeight/2;
			if (headerOnly) {
				eBarPosY = eBarHeaderInscriptionY;
			}
			
			c.beginPath();
			c.setStrokeStyle(style);
			c.setLineWidth(averageLineWidth);
			c.moveTo(eBarStartX + eBarPosX, eBarPosY);	// averageLineWidth has not to be considered
//			c.lineTo(eBarStartX + eBarOtherPosX - xIncrementPerRow*(rowDiff-increment), eBarStartY + eBarHeight/2 - height*increment);	// not correct
			c.lineTo(eBarStartX + eBarOtherPosX, eBarPosY - height*rowDiff);
			c.stroke();
		}
		
		private void setValueAverageConnectLast() {
			setValueAverageConnect(
					averageElement.getRow(), averageElement.getValue(),
					averageElement.getRowLast(), averageElement.getValueLast(), averageElement.getCriterionLast().getWorst(), averageElement.getCriterionLast().getBest(),
					averageElement.getStyle());
		}
		
		public void setValueAverageConnectLast(EvCriterion<Ev, CriterionShortIdentifier> criterionLast, IdCriterionValue<CriterionShortIdentifier> valueLast, int rowLast) {
			assert null != criterionLast;
			assert -1 < rowLast;
			
			averageElement.setLast(criterionLast, valueLast, rowLast);
			
			setValueAverageConnectLast();
		}
		
		private void setValueAverageConnectNext() {
			setValueAverageConnect(
					averageElement.getRow(), averageElement.getValue(),
					averageElement.getRowNext(), averageElement.getValueNext(), averageElement.getCriterionNext().getWorst(), averageElement.getCriterionNext().getBest(),
					averageElement.getStyle());
		}
		
		public void setValueAverageConnectNext(EvCriterion<Ev, CriterionShortIdentifier> criterionNext, IdCriterionValue<CriterionShortIdentifier> valueNext, int rowNext) {
			assert null != criterionNext;
			assert -1 < rowNext;
			
			averageElement.setNext(criterionNext, valueNext, rowNext);
			
			setValueAverageConnectNext();
		}
		
		/**
		 * 
		 * @param posX
		 * @param posY
		 * @return the criterionValue which average is displayed at the given position or null when at the given position is no average
		 */
		public IdCriterionValue<CriterionShortIdentifier> getAverageCriterionValueFromPosition(int posX, int posY) {
			assert 0 <= posX;
			assert 0 <= posY;
			assert width >= posX;
			assert height >= posY;
			
//			for (;;) {
			if (null != averageElement.getValue()) {
				
				IdCriterionValue<CriterionShortIdentifier> criterionValue = averageElement.getValue();
				int averageX = eBarStartX + valueToWidth(criterionValue.getAverage(), getWorst(), getBest());
				int averageY = eBarStartY + eBarHeight/2;
				
				if (averageX - averageHoveringWidth/2 <= posX && averageX + averageHoveringWidth/2 >= posX &&
					averageY - averageHoveringHeight/2 <= posY && averageY + averageHoveringHeight/2 >= posY) {
					return criterionValue;
				}
			}
			
			return null;
		}
		
		public int getHorizontalPositionOfAverage(IdCriterionValue<CriterionShortIdentifier> criterionValue) {
			assert null != criterionValue;
			
			return eBarStartX + valueToWidth(criterionValue.getAverage(), getWorst(), getBest());
		}
		
		public int getVerticalPositionOfAverage() {
			return eBarStartY + eBarHeight/2;
		}
		
		
		// methods if EvBar is a header only (legend|explanation about what is to see)
		
		public void setHeaderInscription() {
			
			c.setFillStyle(averageElement.getStyle());
			c.setFont(standardFont);
			c.setTextAlign(Context2d.TextAlign.CENTER);
			c.setTextBaseline(Context2d.TextBaseline.BOTTOM);
			c.fillText("Average", eBarStartX + valueToWidth(averageElement.getValue().getAverage(), getWorst(), getBest()), eBarHeaderInscriptionY);
			
		}
		
		
		/**
		 * stores the last element of the table that has a value, the current, and the next element of the table that has a value
		 * @author michael
		 *
		 */
		private class EvBarAverageElement {
			
			private EvCriterion<Ev, CriterionShortIdentifier> criterion = null;
			IdCriterionValue<CriterionShortIdentifier> value = null;
			private int row = -1;
			private EvCriterion<Ev, CriterionShortIdentifier> criterionLast = null;
			IdCriterionValue<CriterionShortIdentifier> valueLast = null;
			private int rowLast = -1;
			private EvCriterion<Ev, CriterionShortIdentifier> criterionNext = null;
			IdCriterionValue<CriterionShortIdentifier> valueNext = null;
			private int rowNext = -1;
			
			private String style = null;
			
			public EvCriterion<Ev, CriterionShortIdentifier> getCriterion() {
				return criterion;
			}
			
			public IdCriterionValue<CriterionShortIdentifier> getValue() {
				return value;
			}
			
			public int getRow() {
				return row;
			}
			
			public void set(EvCriterion<Ev,CriterionShortIdentifier> criterion, IdCriterionValue<CriterionShortIdentifier> value, int row) {
				assert null != criterion;
				assert -1 < row;
				assert criterion.getIdentifier().equals(value.getIdentifier()) : "value does not fit to criterion";
				
				this.criterion = criterion;
				this.value = value;
				this.row = row;
			}
			
			public EvCriterion<Ev, CriterionShortIdentifier> getCriterionLast() {
				return criterionLast;
			}
			
			public IdCriterionValue<CriterionShortIdentifier> getValueLast() {
				return valueLast;
			}
			
			public int getRowLast() {
				return rowLast;
			}
			
			public void setLast(EvCriterion<Ev,CriterionShortIdentifier> criterionLast, IdCriterionValue<CriterionShortIdentifier> valueLast, int rowLast) {
				assert null != criterionLast;
				assert -1 < rowLast;
				assert criterionLast.getIdentifier().equals(valueLast.getIdentifier()) : "valueLast does not fit to criterionLast";
				
				this.criterionLast = criterionLast;
				this.valueLast = valueLast;
				this.rowLast = rowLast;
			}
			
			public EvCriterion<Ev, CriterionShortIdentifier> getCriterionNext() {
				return criterionNext;
			}
			
			public IdCriterionValue<CriterionShortIdentifier> getValueNext() {
				return valueNext;
			}
			
			public int getRowNext() {
				return rowNext;
			}
			
			public void setNext(EvCriterion<Ev,CriterionShortIdentifier> criterionNext, IdCriterionValue<CriterionShortIdentifier> valueNext, int rowNext) {
				assert null != criterionNext;
				assert -1 < rowNext;
				assert criterionNext.getIdentifier().equals(valueNext.getIdentifier()) : "valueNext does not fit to criterionNext";
				
				this.criterionNext = criterionNext;
				this.valueNext = valueNext;
				this.rowNext = rowNext;
			}
			
			public String getStyle() {
				return style;
			}
			
			public void setStyle(String style) {
				assert null != style;
				
				this.style = style;
			}
		}
	}
	
	/**
	 * used to display details about EvBars (averages of CriterionValues)
	 * @author michael
	 *
	 */
	private class CriterionValueViewPopup extends SimplePanel {
		
		private IdCriterionValue<CriterionShortIdentifier> criterionValue = null;
		
		public CriterionValueViewPopup() {
			
			setVisible(false);
			
			// style
			setStyleName(EvStyle.eCriterionTableCanvasEvBarPopup);
			
		}
		
		/**
		 * displays informations about the given criterionValue at the given position
		 * TODO add name of element to which the value belongs
		 * @param criterionValue
		 * @param left
		 * @param top
		 */
		public void setValueAndShow(IdCriterionValue<CriterionShortIdentifier> criterionValue, int left, int top) {
			assert null != criterionValue;
			
			if (this.criterionValue == criterionValue) {
				return;
			}
			
			this.criterionValue = criterionValue;
			
			FlowPanel panel = new FlowPanel();
			Label averageLabel = new Label("Average: " + NumberFormat.getFormat("0.00").format(criterionValue.getAverage()));
			Label evsCountLabel = new Label("(" + criterionValue.getVotes() + " evs)");
			
			panel.add(averageLabel);
			panel.add(evsCountLabel);
			setWidget(panel);
			
			getElement().getStyle().setMarginLeft(left, Unit.PX);
			getElement().getStyle().setMarginTop(top, Unit.PX);
			
			setVisible(true);
		}
		
		/**
		 * resets the widget if a CriterionValue was set before
		 */
		public void resetAndHide() {
			if (null != criterionValue) {
				criterionValue = null;
				setVisible(false);
			}
		}
	}
}
