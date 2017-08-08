/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.kafka.consumers;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.aerogear.unifiedpush.kafka.KafkaClusterConfig;
import org.jboss.aerogear.unifiedpush.kafka.test.MockProviders;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.debezium.kafka.KafkaCluster;
import io.debezium.util.Testing;

/**
 * Test cases for {@link InstallationMetricsConsumer#consume(String, String)}
 * method.
 */
@RunWith(Arquillian.class)
public class InstallationMetricsConsumerTest {

	private KafkaProducer producer;

	private File dataDir;
	private KafkaCluster kafkaCluster;

	@Deployment
	public static JavaArchive createDeployment() {
		//TODO fix this
		return ShrinkWrap.create(JavaArchive.class).addPackage(InstallationMetricsConsumer.class.getPackage())
				.addClasses(KafkaClusterConfig.class, MockProviders.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Before
	public void beforeEach() throws IOException {

		dataDir = Testing.Files.createTestingDirectory("cluster");
		kafkaCluster = new KafkaCluster().usingDirectory(dataDir).withPorts(2181, 9092);

		kafkaCluster.addBrokers(1).startup();
		kafkaCluster.createTopic(KafkaClusterConfig.KAFKA_INSTALLATION_TOPIC, 2, 1);

	}

	@After
	public void afterEach() {
		close();
		kafkaCluster.shutdown();
		Testing.Files.delete(dataDir);
	}

	@Test
	public void consumeTest() {
		producer = new KafkaProducer<>(producerProps());
		// TODO change test values for key/value
		producer.send(new ProducerRecord(KafkaClusterConfig.KAFKA_INSTALLATION_TOPIC, "test", "test"));
		// TODO test if updateAnalytics is executed
	}

	protected void close() {
		if (producer != null) {
			producer.close();
		}
	}

	private Properties producerProps() {
		Properties props = kafkaCluster.useTo().getProducerProperties(null);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return props;
	}
}
