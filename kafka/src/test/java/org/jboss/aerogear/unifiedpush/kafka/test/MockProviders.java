package org.jboss.aerogear.unifiedpush.kafka.test;

import static org.mockito.Mockito.mock;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.jboss.aerogear.unifiedpush.dao.PushMessageInformationDao;
import org.jboss.aerogear.unifiedpush.dao.VariantMetricInformationDao;
import org.jboss.aerogear.unifiedpush.service.metrics.PushMessageMetricsService;

@RequestScoped
public class MockProviders {

	private PushMessageInformationDao pushMessageInformationDao = mock(PushMessageInformationDao.class);
	private PushMessageMetricsService pushMessageMetricsService = mock(PushMessageMetricsService.class);
	private VariantMetricInformationDao variantMetricInformationDao = mock(VariantMetricInformationDao.class);

	@Produces
	public PushMessageInformationDao getPushMessageInformationDao() {
		return pushMessageInformationDao;
	}

	@Produces
	public VariantMetricInformationDao getVariantMetricInformationDao() {
		return variantMetricInformationDao;
	}

	@Produces
	public PushMessageMetricsService getPushMessageMetricsService() {
		return pushMessageMetricsService;
	}
}
