/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.gov.gchq.gaffer.sparkaccumulo.operation.handler;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import uk.gov.gchq.gaffer.spark.SparkConstants;

public class SparkSessionProvider {
    private static SparkSession sparkSession;
    private static JavaSparkContext javaSparkContext;

    public static synchronized SparkSession getSparkSession() {
        if (null == sparkSession) {
            sparkSession = SparkSession.builder()
                    .master("local")
                    .appName("spark-accumulo-library-tests")
                    .config(SparkConstants.SERIALIZER, SparkConstants.DEFAULT_SERIALIZER)
                    .config(SparkConstants.KRYO_REGISTRATOR, SparkConstants.DEFAULT_KRYO_REGISTRATOR)
                    .config(SparkConstants.DRIVER_ALLOW_MULTIPLE_CONTEXTS, true)
                    .getOrCreate();
            javaSparkContext = new JavaSparkContext(sparkSession.sparkContext());
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    sparkSession.stop();
                }
            });
        }
        return sparkSession;
    }

    public static synchronized JavaSparkContext getJavaSparkContext() {
        getSparkSession();
        return javaSparkContext;
    }
}