package de.otto.wsi.lisa.web.presenter;

import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.cdi.NormalUIScoped;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table;
import de.otto.wsi.authentication.AuthenticationUtil;
import de.otto.wsi.common.component.exception.IncorrectDataException;
import de.otto.wsi.common.component.helper.PrintSortHelper;
import de.otto.wsi.common.component.util.CommonServiceUtil;
import de.otto.wsi.common.component.util.Constants;
import de.otto.wsi.common.component.util.I18N;
import de.otto.wsi.common.component.util.StringFormat;
import de.otto.wsi.common.component.util.print.DailyorderPickupLabelStream;
import de.otto.wsi.common.component.util.print.DailyorderPrintLabelStream;
import de.otto.wsi.common.domain.model.UserSessionDetails;
import de.otto.wsi.lisa.domain.ELSService;
import de.otto.wsi.lisa.domain.model.WarehouseDetails;
import de.otto.wsi.lisa.web.view.els.ELSPrintStream;
import de.otto.wsi.lisa.web.view.els.ELSView;
import de.otto.wsi.lisa.web.view.els.FilterLayout;
import de.otto.wsi.onda.domain.OrdersService;
import de.otto.wsi.onda.domain.model.DeliveryInfo;
import de.otto.wsi.onda.domain.model.OrderItemDetails;
import de.otto.wsi.onda.domain.model.OrderSearchCriteria;

public class ELSPresenter {


	private static final String DATE_FORMAT_DD_MM_YYYY = "dd.MM.yyyy";
    private static final String DATE_FORMAT_V2 = "yy-MM-dd HH:mm:ss";
    private static final int SORT_NO21 = 21;
    private static final int CUSTOMER_NUMBER_START_INDEX = 5;
    private static final int CUSTOMER_NUMBER_END_INDEX = 8;
    private static final String PDF_EXT = ".pdf";
    private static final int MIN_LENGTH_EXPECTED_CUSTOMER_ACCOUNT_NUMBER = 5;
    private static final int POSITION_EXPECTED_CUSTOMER_ACCOUNT_NUMBER = 8;
    private static final Logger LOGGER = LoggerFactory.getLogger(ELSPresenter.class);
    private static final String DATE_01_01_0001 = "01.01.0001";
    private static final String POSITION = "Position";
    private static final String ORDER_NUMBER = "OrderNumber";
    private static final String ORDER_DATE = "OrderDate";
    private static final String ENTRY_NUMBER = "EntryNumber";
    private static final String SHIPMENT_IDENTIFICATION_NUMBER = "ShipmentIdentificationNumber";
    private static final String ITEM_NO = "itemNo";
    private static final String ARTICLE_DESCRIPTION = "ArticleDescription"; final String SIZE = "Size";
	private static final String LENGTH = "Length";
	private static final String BREADTH = "Breadth";
	private static final String HEIGHT = "Height";
	private static final String SQM = "SQM";
	private static final String WAREHOUSE_LOCATION = "WarehouseLocation";
	public static final String DELETE_ROW = "DeleteRow";
	private static final String ELS = "Els";
	private static final String HEADER_SELECT = "HeaderSelect";
	private static final String CHK_ROW_SELECT = "chkRowSelect";
	private static final String DELIVERY_DATE = "deliverDate";
	private static final String SHIPPING_DATE = "shippingdate";
	private static final String SPEDITION = "spedition";   
	private static final String TOTAL_NUMBER_OF_POSITIONS = "totalNumberOfPositions";
	private static final String ORDER_STATUS_TA_ALL = "TA_ALL";
    private static final String MESSAGE_NO_RECORDS_SELECTED = "diva.global.error.norecordselected";
    private Map<Long, OrderItemDetails> cachedOrders = new LinkedHashMap<>();
    private List<OrderItemDetails> orderDataELS = new ArrayList<>();
    private List<CheckBox> checkBoxesSelectedForELS = new ArrayList<>();
    private boolean isELSNumberSaved = true;
    private String unsavedElsNumber = "";
    private String warehouse = "";
	private ELSView view;
    private CommonServiceUtil commonServiceUtil;
    private OrdersService orderService;
    private ELSService service;
    private Button btnRowSelect;
    private int positionCount;
    private int selectedPositionCount;

	public ELSPresenter() {
		super();
	}

	public void initialize(ELSView elsView, CommonServiceUtil commonServiceUtil, ELSService service,
			OrdersService orderService) {
		this.view = elsView;
		this.commonServiceUtil = commonServiceUtil;
		this.service = service;
		this.orderService = orderService;
	}

	public void populateSubNumberList(String fictitiousSupplierNumber) {
		ComboBox cmbSubnumber = view.getELSFilterLayout().getCmbSubNumber();
		cmbSubnumber.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
		cmbSubnumber.setNullSelectionAllowed(false);

		List<String> subNumberList = commonServiceUtil.getSubNumberList(fictitiousSupplierNumber);
		if (subNumberList != null && !subNumberList.isEmpty()) {
			if (subNumberList.size() > 1) {
				cmbSubnumber.addItem("");
				cmbSubnumber.setItemCaption("", I18N.get("onda.dailyorders.orderstatus.all"));
			}
			for (String subNumber : subNumberList) {
				cmbSubnumber.addItem(subNumber);
				cmbSubnumber.setItemCaption(subNumber, subNumber);
			}
			cmbSubnumber.setValue(cmbSubnumber.getItemIds().iterator().next());
			cmbSubnumber.setPageLength(cmbSubnumber.size());
		}
	}

	public void setDataToComponent() {
		UserSessionDetails userDetails = AuthenticationUtil.getUserSessionDetails();
		populateSubNumberList(userDetails.getFictitiousSupplierNumber());
	}

	public List<OrderItemDetails> searchDailyOrders(boolean isELSSearch, boolean isPrint, boolean printDO) {

		List<OrderItemDetails> orderData = null;
		try {
		    if(isELSSearch) {
		      view.getContentLayout().checkCacheStatusAndSetELSButtons();
		    } else {
		      setAddELSButtonTo(true);
		    }
			UserSessionDetails userDetails = AuthenticationUtil.getUserSessionDetails();
			FilterLayout filter = view.getELSFilterLayout();
			DateFormat searchformat = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY, VaadinSession.getCurrent().getLocale());			
			OrderSearchCriteria searchCriteria = new OrderSearchCriteria();
			searchCriteria.setSupplierNo(userDetails.getFictitiousSupplierNumber());
			searchCriteria.setSupplierSubNo((filter.getCmbSubNumber().getValue() == null) ? ""
                : filter.getCmbSubNumber().getValue().toString());
			searchCriteria.setCustomerAccountNo(StringFormat.getStringDigitWithZero(filter.getTxtCustomerNumber().getValue(), 
                CUSTOMER_NUMBER_START_INDEX, CUSTOMER_NUMBER_END_INDEX));
			searchCriteria.setOrderDate((filter.getDtfOrderDate().getValue() == null) ? ""
                : searchformat.format(filter.getDtfOrderDate().getValue()));
			searchCriteria.setArticleNo(filter.getTxtItemNumber().getValue());
			searchCriteria.setEntryNo(filter.getTxtEntryNumber().getValue());
			searchCriteria.setSortNo(SORT_NO21);
			searchCriteria.setOrderNo(filter.getTxtOrderNumber().getValue());
            String deliveryDate = (filter.getDtfDeliveryDate().getValue() == null) ? ""
                    : searchformat.format(filter.getDtfDeliveryDate().getValue());
            String elsNo = StringUtils.defaultIfEmpty(filter.getTxtELSNumber().getValue(), "");
			orderData = service.getOrdersData(searchCriteria, true, deliveryDate, elsNo, isELSSearch, 
			    orderService);
			service.setInternalItemDetails(orderData);
			
			if (isPrint && orderData != null && !orderData.isEmpty()) {
				String tempWarehouse = orderData.get(0).getDeliveryInfo().getDeLiveryLocation();
				String lkwNr = service.getLKWNr(orderData.get(0).getElsNo());
				WarehouseDetails warehouseDetails = service.getWarehouseDetail(tempWarehouse);
				Date newDate = new Date();
	            ELSPrintStream elsPrintStream =
	                new ELSPrintStream(orderData, warehouseDetails, lkwNr, null);
	            InputStream stream = elsPrintStream.getStreamData();
	            StreamResource.StreamSource source = new ELSPrintStream(orderData, warehouseDetails, lkwNr, stream);
				displayPDF(source, "ELS_" + tempWarehouse + newDate.getTime() + PDF_EXT);
			} else if (isPrint && orderData != null && orderData.isEmpty()) {
				view.getELSFilterLayout().alertbox("diva.global.error.norecordfound");
			}
			if (!isPrint && !printDO) {
				setOrderDataToContainer(orderData, isELSSearch);
                filter.setPrintEnabled(true);
			}
		} catch (ParseException | SQLException e) {
			LOGGER.error(e.getMessage(), e);
	        view.getELSFilterLayout().alertbox("diva.global.error.sqlexception");	      
		} catch(IncorrectDataException e) {
		  LOGGER.error("Error while creating PDF: ", e);
          view.getContentLayout().alertbox(I18N.get("diva.els.print.caution"), false);
        }
		return orderData;
	}

	private void setOrderDataToContainer(List<OrderItemDetails> orderData, boolean isELSSearch) throws ParseException {
		IndexedContainer container = view.getContentLayout().createContainer();
		if(!isELSSearch) {
		  setDailyOrderDataToContainer(container, orderData);
		} else {
		  setELSDataToContainer(container, orderData);
		}
		view.getContentLayout().setContentPanel(container, isELSSearch);
		view.getContentLayout().getContentData().setData(orderData);
	}
	
  private void setDailyOrderDataToContainer(IndexedContainer container, List<OrderItemDetails> orderData) {
      int totalRecordsOnTable = orderData.size();
      Object itemId;
        for (OrderItemDetails orderDetails : orderData) {
            itemId = container.addItem();
            String orderDate =
                getDateInGermanFormat(orderDetails.getOrderDate(), new SimpleDateFormat(DATE_FORMAT_V2));
            int i = 0;
            CheckBox chkRowSelect = new CheckBox();
            chkRowSelect.setId(CHK_ROW_SELECT + ++i);
            chkRowSelect.setData(orderDetails);
            chkRowSelect.addValueChangeListener(checkboxValueChangeAction(totalRecordsOnTable)); 
            if(!cachedOrders.isEmpty() && cachedOrders.containsKey(orderDetails.getAutoKey())) {
              chkRowSelect.setValue(true);
              chkRowSelect.setEnabled(false);
            }
            container.getContainerProperty(itemId, HEADER_SELECT).setValue(chkRowSelect);
            container.getContainerProperty(itemId, POSITION).setValue(orderDetails.getPositionTA());
            container.getContainerProperty(itemId, ORDER_DATE).setValue(compareAndGetCorrectDate(orderDate, DATE_01_01_0001));

            container.getContainerProperty(itemId, ITEM_NO).setValue(orderDetails.getArticleNo());
            container.getContainerProperty(itemId, ARTICLE_DESCRIPTION).setValue(orderDetails.getDescription());
            container.getContainerProperty(itemId, SIZE).setValue(orderDetails.getSize());
            float breadth = orderDetails.getPhysicalAttributes().getWidth();
            container.getContainerProperty(itemId, BREADTH).setValue((breadth == 0) ? "" : Float.toString(breadth));
            float height = orderDetails.getPhysicalAttributes().getHeight();
            container.getContainerProperty(itemId, HEIGHT).setValue((height == 0) ? "" : Float.toString(height));
            String sqm = orderDetails.getAdditionalInfo().getMengeLFDMeter();
            container.getContainerProperty(itemId, SQM).setValue(sqm);
            container.getContainerProperty(itemId, WAREHOUSE_LOCATION)
                    .setValue(orderDetails.getDeliveryInfo().getDeLiveryLocation());
            container.getContainerProperty(itemId, ENTRY_NUMBER).setValue(Long.toString(orderDetails.getEntryNo()));
            container.getContainerProperty(itemId, ORDER_NUMBER).setValue(orderDetails.getOrderNo());
            container.getContainerProperty(itemId, SHIPMENT_IDENTIFICATION_NUMBER)
                    .setValue(orderDetails.getPackingUnitInfo().getPacketIdentificationNo());
            String elsMarker = " ";
            if (orderDetails.getElsNo() != null && !orderDetails.getElsNo().trim().isEmpty()) {
                elsMarker = "X";
            }
            container.getContainerProperty(itemId, ELS).setValue(elsMarker);
            float length = orderDetails.getPhysicalAttributes().getLength();
            container.getContainerProperty(itemId, LENGTH).setValue((length == 0) ? "" : Float.toString(length));
        }
        setAddELSButtonTo(true);
        view.getContentLayout().checkCacheStatusAndSetELSButtons();
    
	}
	
    private void setELSDataToContainer(IndexedContainer container, List<OrderItemDetails> orderData) 
	    throws ParseException {
	  SimpleDateFormat dbDateInputFormat = new SimpleDateFormat(DATE_FORMAT_V2);
      SimpleDateFormat dbDateOutputFormat = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY);
      String localELSNo = "";
      int totalOrderPositions = 0;
      Object itemId = null;
      for(OrderItemDetails orderDetails: orderData) {
        if(!localELSNo.equals(orderDetails.getElsNo())) {
          localELSNo = orderDetails.getElsNo();
          totalOrderPositions = 0;
          itemId = container.addItem();
          String deliveryDate =
              getDateInGermanFormat(orderDetails.getDeliveryDate(), new SimpleDateFormat(DATE_FORMAT_V2));
          String shipmentDate =
            getDateInGermanFormat(
                StringUtils.defaultIfEmpty(orderDetails.getShipmentDate(), ""),
                    new SimpleDateFormat(DATE_FORMAT_V2));
          container.getContainerProperty(itemId, ELS).setValue(orderDetails.getElsNo());
          container.getContainerProperty(itemId, DELIVERY_DATE).setValue(deliveryDate);
          container.getContainerProperty(itemId, WAREHOUSE_LOCATION).setValue(
              orderDetails.getDeliveryInfo().getDeLiveryLocation());
          container.getContainerProperty(itemId, SPEDITION).setValue(orderDetails.getSpedition());
          container.getContainerProperty(itemId, SHIPPING_DATE).setValue(shipmentDate);
        }
        if(!localELSNo.isEmpty()) {
          container.getContainerProperty(itemId, TOTAL_NUMBER_OF_POSITIONS).setValue(
              String.valueOf(++totalOrderPositions));
        }
      }
      setAddELSButtonTo(false);
      view.getContentLayout().checkCacheStatusAndSetELSButtons();
	}
    
  public void addELS() {
    checkBoxesSelectedForELS = getSelectedCheckBoxes();
    orderDataELS = checkBoxesSelectedForELS.stream()
                             .map(cb -> (OrderItemDetails)cb.getData())
                             .collect(Collectors.toList());
    if (!(orderDataELS.isEmpty() || orderDataELS == null)) {
      if (!isWarehouseDifferent(orderDataELS)) {
        long count = getExistingELSCountIfAny(orderDataELS);
        if (count > 0) {
          view.getContentLayout().setSaveSuccess(false);
          view.getContentLayout().setAlreadyexistflag(true);
          view.getContentLayout()
              .alertbox(count + " " + I18N.get("diva.els.message.of") + " "
                  + orderDataELS.size() + " " + I18N.get("diva.els.error.elsexists"),
                  true);
        } else {
          cacheOrdersForELS(orderDataELS, getELSNo());
        }
      } else {
        view.getContentLayout().alertbox(I18N.get("diva.global.error.different.warehouse"), false);
        setCheckBoxes(checkBoxesSelectedForELS, orderDataELS, false);
      }
    } else {
      view.getContentLayout().setSaveSuccess(false);
      view.getContentLayout().alertbox(I18N.get(MESSAGE_NO_RECORDS_SELECTED), false);
    }
  }
  
  private List<CheckBox> getSelectedCheckBoxes() {
    Table contentData = view.getContentLayout().getContentData();
    IndexedContainer iContainer = (IndexedContainer) contentData.getContainerDataSource();
    Collection<?> itemIds = contentData.getContainerDataSource().getItemIds();
    List<CheckBox> checkBoxes = new ArrayList<>();
    itemIds.forEach(itemId -> {
      if (iContainer.getContainerProperty(itemId, HEADER_SELECT).getValue() instanceof CheckBox) {
        CheckBox chkSelect =
            (CheckBox) iContainer.getContainerProperty(itemId, HEADER_SELECT).getValue();
        if (Boolean.TRUE.equals(chkSelect.getValue())) {
          OrderItemDetails orderItem = (OrderItemDetails) chkSelect.getData();
          if(!cachedOrders.containsKey(orderItem.getAutoKey())) {
            if (warehouse == null || warehouse.isEmpty()) {
              warehouse = orderItem.getDeliveryInfo().getDeLiveryLocation();
            }
            checkBoxes.add(chkSelect);
          }
        }
      }
    });
    return checkBoxes;
  }
  
  private boolean isWarehouseDifferent(List<OrderItemDetails> orderData) {
    List<String> warehouseList = orderData.stream()
                                    .map(OrderItemDetails::getDeliveryInfo)
                                    .map(DeliveryInfo::getDeLiveryLocation)
                                    .filter(str -> !this.warehouse.equals(str))
                                    .collect(Collectors.toList());
    return !warehouseList.isEmpty();
  }
  
  private long getExistingELSCountIfAny(List<OrderItemDetails> orderData) {
    return orderData.stream().filter(o -> !StringUtils.isEmpty(o.getElsNo())).count();
  }
  
  public void appproveAndCacheOrders(boolean isApproved) {
    if(isApproved) {
      cacheOrdersForELS(orderDataELS, getELSNo());
    } else {
      cacheOrdersForELS(orderDataELS.stream()
                                    .filter(order -> StringUtils.isEmpty(order.getElsNo()))
                                    .collect(Collectors.toList()), getELSNo());
    }
  }
  
  private String getELSNo() {
    if (isELSNumberSaved()) {
      unsavedElsNumber = service.getELSNo();
      setELSNumberSaved(false);
    }
    return unsavedElsNumber;
  }

  private void cacheOrdersForELS(List<OrderItemDetails> orderData, String newELSNo) {
    orderData.forEach(order -> {
      order.setNewELSNo(newELSNo);
      cachedOrders.put(order.getAutoKey(), order);
    });
    view.getContentLayout().setSaveSuccess(true);
    view.getContentLayout().setPositionsSelectedForELS(orderData.size());
    view.getContentLayout().setCacheEmpty(cachedOrders.isEmpty());
    setCheckBoxes(checkBoxesSelectedForELS, orderData, true);
    orderDataELS.clear();
    checkBoxesSelectedForELS.clear();
    
    view.getContentLayout().getContentData().setColumnHeader(HEADER_SELECT,
        Constants.HEADER_SELECT_UNCHECKED);
  }

  private void setCheckBoxes(List<CheckBox> checkBoxes, List<OrderItemDetails> orderData, boolean value) {
    List<Long> autoKeys = orderData.stream()
                                    .map(OrderItemDetails::getAutoKey)
                                    .collect(Collectors.toList());
    checkBoxes.forEach(cb -> {
      if (autoKeys.contains(((OrderItemDetails)cb.getData()).getAutoKey())) {
        cb.setValue(value);
        cb.setEnabled(!value);
      } else {
        cb.setValue(!value);
        cb.setEnabled(value);
      }
    });
  }

    /**
     * Method for selecting all Checkboxes
     * @param selected - boolean parameter. 
     * If parameter passed is true then all checkboxes are selected else all checkboxes are unselected.
     */
    public void toggleSelect(boolean selected) {
      Table contentData = view.getContentLayout().getContentData();
      IndexedContainer iContainer = (IndexedContainer) contentData.getContainerDataSource();
      Collection<?> itemIds = contentData.getContainerDataSource().getItemIds();
      for (Object itemId : itemIds) {
        if (iContainer.getContainerProperty(itemId, HEADER_SELECT).getValue() instanceof CheckBox) {
          CheckBox chkSelect =
              (CheckBox) iContainer.getContainerProperty(itemId, HEADER_SELECT).getValue();
          OrderItemDetails orderItem = (OrderItemDetails) chkSelect.getData();
          if (!cachedOrders.containsKey(orderItem.getAutoKey())) {
            chkSelect.setValue(selected);
            }
          }
        }
     }
    
    /**
     * Method for Checkbox value change actions
     * @param totalRecordsOnTable
     *              represents the total number of orders in the table displayed on UI.
     */
    private ValueChangeListener checkboxValueChangeAction(int totalRecordsOnTable) {
        return event -> {
            boolean value = (boolean) event.getProperty().getValue();
            view.getContentLayout().getContentData()
                .setColumnHeader(HEADER_SELECT, Constants.HEADER_SELECT_UNCHECKED);
            view.getContentLayout().setSelectedStatus(false);

            if (value) {
                positionCount++;
                selectedPositionCount++;
                if (totalRecordsOnTable == positionCount
                    || totalRecordsOnTable == selectedPositionCount) {
                    view.getContentLayout().getContentData()
                        .setColumnHeader(HEADER_SELECT, Constants.HEADER_SELECT_CHECKED);
                    view.getContentLayout().setSelectedStatus(true);
                }
            } else {
                selectedPositionCount--;
                positionCount--;
            }
        };
    }
    
	public void displayELS() {
		if (this.cachedOrders != null && !this.cachedOrders.isEmpty()) {
			IndexedContainer container = view.getContentLayout().createContainer();
			Object itemId;
			for (OrderItemDetails orderDetails : cachedOrders.values()) {
				itemId = container.addItem();
        String orderDate =
            getDateInGermanFormat(
                orderDetails.getOrderDate(), new SimpleDateFormat(DATE_FORMAT_V2));
				int i = 0;
				CheckBox chkRowSelect = new CheckBox();
				chkRowSelect.setId(CHK_ROW_SELECT + ++i);
				chkRowSelect.setData(orderDetails);
				container.getContainerProperty(itemId, ORDER_DATE)
						.setValue(compareAndGetCorrectDate(orderDate, DATE_01_01_0001));
				container.getContainerProperty(itemId, ITEM_NO).setValue(orderDetails.getArticleNo());
				container.getContainerProperty(itemId, ARTICLE_DESCRIPTION).setValue(orderDetails.getDescription());
				container.getContainerProperty(itemId, WAREHOUSE_LOCATION)
						.setValue(orderDetails.getDeliveryInfo().getDeLiveryLocation());
				container.getContainerProperty(itemId, ENTRY_NUMBER).setValue(Long.toString(orderDetails.getEntryNo()));
				container.getContainerProperty(itemId, ORDER_NUMBER).setValue(orderDetails.getOrderNo());
				container.getContainerProperty(itemId, SHIPMENT_IDENTIFICATION_NUMBER)
						.setValue(orderDetails.getPackingUnitInfo().getPacketIdentificationNo());
				container.getContainerProperty(itemId, ELS).setValue(orderDetails.getNewELSNo());
			}
			view.getDisplayWindow().setContentPanel(container);
			view.getDisplayWindow().getContentData().setData(cachedOrders.values());
			view.getDisplayWindow().manageLocale();
			view.getContentLayout().displayELS(view.getDisplayWindow());
		} else {
			view.getContentLayout().alertbox(I18N.get(MESSAGE_NO_RECORDS_SELECTED), false);
		}
	}

	public void setELSButtonsTo(boolean enabled) {
		view.getContentLayout().setELSButtonsTo(enabled);
	}

	public void setAddELSButtonTo(boolean enabled) {
		view.getContentLayout().setELSAddButtonTo(enabled);
	}

	public void createELS() {
		view.getContentLayout().manageLocale();
		if (this.cachedOrders != null && !this.cachedOrders.isEmpty()) {
			IndexedContainer container = view.getContentLayout().createContainer();
			Object itemId;
			
			for (OrderItemDetails orderDetails : cachedOrders.values()) {
				itemId = container.addItem();
				SimpleDateFormat dbDateInputFormat = new SimpleDateFormat(DATE_FORMAT_V2);
				if(view.getCreateWindow().getTxtELSNumber().getValue().isEmpty() ||
				    view.getCreateWindow().getTxtELSNumber().getValue() == null) {
				  view.getCreateWindow().getTxtELSNumber().setValue(orderDetails.getNewELSNo());
				} else {
				  view.getCreateWindow().getTxtELSNumber().setValue(
				      view.getCreateWindow().getTxtELSNumber().getValue());
				}
				
				view.getCreateWindow().getTxtSupplier()
						.setValue(String.valueOf(AuthenticationUtil.getUserSessionDetails().getSupplierNumber()));
				view.getCreateWindow().setWarehouse(orderDetails.getDeliveryInfo().getDeLiveryLocation());
        String orderDate =
            getDateInGermanFormat(
                orderDetails.getOrderDate(), new SimpleDateFormat(DATE_FORMAT_V2));
				int i = 0;
				container.getContainerProperty(itemId, ORDER_DATE)
						.setValue(compareAndGetCorrectDate(orderDate, DATE_01_01_0001));
				container.getContainerProperty(itemId, ITEM_NO).setValue(orderDetails.getArticleNo());
				container.getContainerProperty(itemId, ARTICLE_DESCRIPTION).setValue(orderDetails.getDescription());
				container.getContainerProperty(itemId, ENTRY_NUMBER).setValue(Long.toString(orderDetails.getEntryNo()));
				container.getContainerProperty(itemId, ORDER_NUMBER).setValue(orderDetails.getOrderNo());
				container.getContainerProperty(itemId, SHIPMENT_IDENTIFICATION_NUMBER)
						.setValue(orderDetails.getPackingUnitInfo().getPacketIdentificationNo());
				container.getContainerProperty(itemId, WAREHOUSE_LOCATION).setValue(orderDetails.getDeliveryInfo().getDeLiveryLocation());
				btnRowSelect = new Button();
				btnRowSelect.setId("onda.els.button.els.row.select");
				btnRowSelect.setStyleName(ValoTheme.BUTTON_PRIMARY);
				btnRowSelect.setClickShortcut(KeyCode.ENTER);
				btnRowSelect.setSizeFull();
				btnRowSelect.setCaption(I18N.get("onda.els.button.remove"));
				btnRowSelect.setId(CHK_ROW_SELECT + ++i);
				btnRowSelect.addClickListener(view.getCreateWindow());
				btnRowSelect.setData(orderDetails);
				container.getContainerProperty(itemId, DELETE_ROW).setValue(btnRowSelect);
			}
			view.getCreateWindow().setContentPanel(container);
			view.getCreateWindow().setElsServcie(service);
	        view.getCreateWindow().manageLocale();
	        view.getContentLayout().createELS(view.getCreateWindow());
		} else {
          view.getContentLayout().alertbox(I18N.get(MESSAGE_NO_RECORDS_SELECTED), false);
        }
	}

	public boolean save(List<OrderItemDetails> orderItemList, String elsNo, String lkwNr) {
		boolean saved = false;
		try {
			boolean exists = service.checkIfELSExists(elsNo);
			LOGGER.debug("Checking ELS NO : {} ", exists);
			if (!exists) {
				service.saveELSData(orderItemList, elsNo,lkwNr);
				saved = true;
			} else {
				view.getCreateWindow().resetFilters();
				saved = false;
				view.getCreateWindow().alertbox(I18N.get("diva.global.error.els.already.els.used"));
			}
		} catch (SQLException s) {
			LOGGER.error("Error occured while saving els no", s);
			saved = false;
			if (s.getMessage().contains("ELS_CONSTRAINT")) {
				view.getCreateWindow().resetFilters();
				view.getCreateWindow().alertbox(I18N.get("diva.global.error.els.already.els.used"));
				return saved;
			}
		}
		return saved;
	}

	public void print(boolean isELSPrintLabels, boolean fromCreateELSWindow) {
	  FilterLayout filter = view.getELSFilterLayout();
	  int sortNo = 4;
	  UserSessionDetails userDetails = AuthenticationUtil.getUserSessionDetails();
	  
	  String customerAccountNumber = StringFormat.getStringDigitWithZero(
	      StringUtils.defaultIfEmpty(filter.getTxtCustomerNumber().getValue(), ""),
	      MIN_LENGTH_EXPECTED_CUSTOMER_ACCOUNT_NUMBER, POSITION_EXPECTED_CUSTOMER_ACCOUNT_NUMBER);
	  String elsNo = "";
	  String deliveryDate = "";
	  String orderDate = "";
	  SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY);
	  if (filter.getDtfOrderDate().getValue() != null) {
	    orderDate = dateFormat.format(filter.getDtfOrderDate().getValue());
	  }

	  OrderSearchCriteria searchCriteria = new OrderSearchCriteria();
	  if(!isELSPrintLabels) {
	    searchCriteria.setSupplierNo(userDetails.getFictitiousSupplierNumber());
	    searchCriteria.setOrderStatus(ORDER_STATUS_TA_ALL);
	    searchCriteria.setSupplierSubNo(filter.getCmbSubNumber().getValue().toString());
	    searchCriteria.setCustomerAccountNo(customerAccountNumber);
	    searchCriteria.setArticleNo(filter.getTxtItemNumber().getValue());
	    searchCriteria.setEntryNo(filter.getTxtEntryNumber().getValue());
	    searchCriteria.setSeIdentNumber(null);
	    searchCriteria.setOrderDate(orderDate);
	    searchCriteria.setCodStatus(null);
	    searchCriteria.setSortNo(sortNo);
	    searchCriteria.setRbDruckauswahl("");
	    searchCriteria.setBillingCode(null);
	    searchCriteria.setSelectWithAbholAuftraege(false);
	    searchCriteria.setBarcodeNo(null);
	    searchCriteria.setOrderNo(filter.getTxtOrderNumber().getValue());
	    elsNo = "";
	    deliveryDate = "";
	  } else {
	    searchCriteria.setSupplierNo(userDetails.getFictitiousSupplierNumber());
	    searchCriteria.setOrderStatus(ORDER_STATUS_TA_ALL);
	    searchCriteria.setSupplierSubNo("");
	    searchCriteria.setCustomerAccountNo("");
	    searchCriteria.setArticleNo("");
	    searchCriteria.setEntryNo("");
	    searchCriteria.setSeIdentNumber(null);
	    searchCriteria.setCompanyNo("");
	    searchCriteria.setOrderDate("");
	    searchCriteria.setCodStatus(null);
	    searchCriteria.setSortNo(sortNo);
	    searchCriteria.setRbDruckauswahl("");
	    searchCriteria.setBillingCode(null);
	    searchCriteria.setSelectWithAbholAuftraege(false);
	    searchCriteria.setBarcodeNo(null);
	    if(!fromCreateELSWindow) {
	      elsNo = filter.getTxtELSNumber().getValue();
	      deliveryDate = filter.getDtfDeliveryDate().getValue() == null ? "" : 
	        dateFormat.format(filter.getDtfDeliveryDate().getValue());
	    } else {
	      elsNo = view.getCreateWindow().getTxtELSNumber().getValue();
	      deliveryDate = view.getCreateWindow().getDtfDeliveryDate().getValue() == null ? "" :
	        dateFormat.format(view.getCreateWindow().getDtfDeliveryDate().getValue());
	    }
	  }
	  List<OrderItemDetails> printDataList = new ArrayList<>();
	  
	  try {
	    printDataList = service.getPrintLabelData(isELSPrintLabels, searchCriteria, elsNo, deliveryDate, 
	        commonServiceUtil);
	    String kos = commonServiceUtil.isKOSEnabled(userDetails.getFictitiousSupplierNumber(),
	        filter.getCmbSubNumber().getValue().toString());
	    boolean isKOSEnabled = kos != null && kos.equalsIgnoreCase("J");
	    printPdf(printDataList, isKOSEnabled, sortNo);
	  } catch (SQLException e) {
	    LOGGER.error(e.getMessage(), e);
	    view.getELSFilterLayout().alertbox("diva.global.error.sqlexception");
	  }
	}
	
	private void printPdf(List<OrderItemDetails> printDataList, boolean isKOSEnabled, int sortNo) {
	  if (!printDataList.isEmpty() && printDataList.size() <= Constants.PRINTING_LIMIT) {
	    StreamResource.StreamSource source = null;
	    Date newDate = new Date();
	    String printOptionSelected = "TAT_KO";
	    int pageBreak = 0;
        PrintSortHelper printSortHelper = new PrintSortHelper();
        printDataList = printSortHelper.getSortedData(printDataList, printOptionSelected);
	    if (isKOSEnabled) {
	      source = new DailyorderPickupLabelStream(printDataList, printOptionSelected, "", commonServiceUtil,
	          sortNo, pageBreak);
	    } else {
	      source = new DailyorderPrintLabelStream(printDataList, sortNo, pageBreak, " ", true, commonServiceUtil);
	    }
	    displayPDF(source, "pl_dailyorders" + newDate.getTime() + PDF_EXT);
      } else if (!printDataList.isEmpty() && printDataList.size() > Constants.PRINTING_LIMIT) {
        view.getELSFilterLayout().alertbox("onda.dailyorders.message.records.more");
      } else {
        view.getELSFilterLayout().alertbox("onda.dailyorders.message.kadruck.required");
      }
	}
	
	private void displayPDF(StreamResource.StreamSource source, String fileName) {
	  if (source != null) {
        StreamResource resource = new StreamResource(source, fileName);
        Embedded embedableContent = new Embedded();
        embedableContent.setSizeFull();
        embedableContent.setType(2);
        embedableContent.setSource(resource);
        view.getELSFilterLayout().getPdfWindow().setContent(embedableContent);
        view.getELSFilterLayout().openPdfWindow();
      }
	}
	

	public void cancelELS() {    
	  resetELSCachingDetails();
		setELSNumberSaved(true);
		unsavedElsNumber = "";
		setELSButtonsTo(false);
		refreshDailyOrders();
	}
	
	public void resetELSCachingDetails() {
	  cachedOrders.clear();
	  orderDataELS.clear();
      checkBoxesSelectedForELS.clear();
	  warehouse = "";
	  view.getContentLayout().setCacheEmpty(true);
	  view.getContentLayout().setPositionsSelectedForELS(0);
	}
	
	public void refreshDailyOrders() {
	  searchDailyOrders(false, false, false);
	  view.getELSFilterLayout().setPrintEnabled(true);
	}
	  
	public void changeStatusOfSelection() {
	  view.getContentLayout().setSelectedStatus(false);
	}
    
	public void clearTablePositionCount() {
	  setPositionCount(0);
	}
    
	public void setSelectedPositionCount() {
	  setSelectedPositionCount(view.getContentLayout().getRecordCount());
	}
    
	public void setSelectedPositionEmpty() {
	  setSelectedPositionCount(0);
	}
	
	public boolean isELSNumberSaved() {
	  return isELSNumberSaved;
	}

	public void setELSNumberSaved(boolean isELSNumberSaved) {
	  this.isELSNumberSaved = isELSNumberSaved;
	}

	public void setUnsavedElsNumber(String unsavedElsNumber) {
	  this.unsavedElsNumber = unsavedElsNumber;
	}

	public String getWarehouse() {
	  return warehouse;
	}
	  
	public void setWarehouse(String warehouse) {
	  this.warehouse = warehouse;
	}
	
	public Button getBtnRowSelect() {
	  return btnRowSelect;
	}

	public Map<Long, OrderItemDetails> getCachedOrders() {
	  return cachedOrders;
	}
	
	private String compareAndGetCorrectDate(String actualDate, String date_01_01_0001) {
	  return StringUtils.equals(actualDate, date_01_01_0001) ? "" : actualDate;
	}

  private String getDateInGermanFormat(String date, SimpleDateFormat fromDateFormat) {
    String dateInGermanFormat = "";
    SimpleDateFormat germanDateFormat = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY);
    try {
        dateInGermanFormat = germanDateFormat.format(fromDateFormat.parse(date));
    } catch (ParseException defaultValue) {
      return "";
    }
    return dateInGermanFormat;
  }

    public void setPositionCount(int positionCount) {
      this.positionCount = positionCount;
    }

    public void setSelectedPositionCount(int selectedPositionCount) {
      this.selectedPositionCount = selectedPositionCount;
    }
    
    public List<OrderItemDetails> getOrderDataELS() {
      return this.orderDataELS;
    }

  }
