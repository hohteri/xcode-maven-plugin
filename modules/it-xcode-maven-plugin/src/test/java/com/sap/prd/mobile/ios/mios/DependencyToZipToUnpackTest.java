/*
 * #%L
 * it-xcode-maven-plugin
 * %%
 * Copyright (C) 2012 SAP AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.sap.prd.mobile.ios.mios;

import java.io.File;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

public class DependencyToZipToUnpackTest extends XCodeTest
{

  @Test
  public void testPrepare() throws Exception
  {

    final String testName = getTestName();

    final File testSourceDirApp = new File(getTestRootDirectory(), "straight-forward/MyApp");
    final File alternateTestSourceDirApp = new File(getTestRootDirectory(), "straight-forward-unpack-zip");


    final File remoteRepositoryDirectory = getRemoteRepositoryDirectory(getClass().getName());

    prepareRemoteRepository(remoteRepositoryDirectory);

    final File zipRepository = new File(new File(".").getCanonicalFile(), "src/test/zipRepository");

    Properties pomReplacements = new Properties();
    pomReplacements.setProperty(PROP_NAME_DEPLOY_REPO_DIR, remoteRepositoryDirectory.getAbsolutePath());
    pomReplacements.setProperty(PROP_NAME_DYNAMIC_VERSION, "1.0." + String.valueOf(System.currentTimeMillis()));
    pomReplacements.setProperty(PROP_NAME_ZIP_REPO_DIR, zipRepository.getAbsolutePath());


    test(testName, testSourceDirApp,
          "com.sap.prd.mobile.ios.mios:xcode-maven-plugin:" + getMavenXcodePluginVersion() + ":prepare-xcode-build",
          THE_EMPTY_LIST,
          null, pomReplacements, new FileCopyProjectModifier(alternateTestSourceDirApp, "pom.xml"));

    File tmp = new File(getTestExecutionDirectory(testName, "MyApp"), "target/xcode-deps/additional-unpacked-artifacts/"
          + Constants.GROUP_ID + "/MyZip/dummy.txt");

    Assert.assertTrue("File '" + tmp + "' not found", tmp.exists());
  }
}
