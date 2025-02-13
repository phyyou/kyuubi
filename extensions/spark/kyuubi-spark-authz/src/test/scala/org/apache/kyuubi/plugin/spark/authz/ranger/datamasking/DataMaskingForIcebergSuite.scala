/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kyuubi.plugin.spark.authz.ranger.datamasking

import org.apache.spark.SparkConf
import org.scalatest.Outcome

import org.apache.kyuubi.Utils

class DataMaskingForIcebergSuite extends DataMaskingTestBase {
  override protected val extraSparkConf: SparkConf = {
    val conf = new SparkConf()

    if (isSparkV31OrGreater) {
      conf
        .set("spark.sql.defaultCatalog", "testcat")
        .set(
          "spark.sql.catalog.testcat",
          "org.apache.iceberg.spark.SparkCatalog")
        .set(s"spark.sql.catalog.testcat.type", "hadoop")
        .set(
          "spark.sql.catalog.testcat.warehouse",
          Utils.createTempDir("iceberg-hadoop").toString)
    }
    conf

  }

  override protected val catalogImpl: String = "in-memory"

  override protected def format: String = "USING iceberg"

  override def beforeAll(): Unit = {
    if (isSparkV31OrGreater) {
      super.beforeAll()
    }
  }

  override def afterAll(): Unit = {
    if (isSparkV31OrGreater) {
      super.afterAll()
    }
  }

  override def withFixture(test: NoArgTest): Outcome = {
    assume(isSparkV31OrGreater)
    test()
  }
}
