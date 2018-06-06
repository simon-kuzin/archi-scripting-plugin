/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.dom.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.script.ArchiScriptException;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;


/**
 * ArchimateModelProxy Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ArchimateModelProxyTests extends EObjectProxyTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateModelProxyTests.class);
    }
    
    private ArchimateModelProxy actualTestProxy;
    
    @Before
    public void runOnceBeforeEachTest() {
        testEObject = IArchimateFactory.eINSTANCE.createArchimateModel();
        testProxy = EObjectProxy.get(testEObject);
        actualTestProxy = (ArchimateModelProxy)testProxy;
    }
    
    @Test
    public void get_ReturnsCorrectProxy() {
        EObjectProxy proxy = EObjectProxy.get(IArchimateFactory.eINSTANCE.createArchimateModel());
        assertTrue(proxy instanceof ArchimateModelProxy);
    }

    @Override
    @Test
    public void getModel() {
        assertEquals(actualTestProxy, testProxy.getModel());
    }
    
    @Override
    @Test
    public void find() {
        EObjectProxy testModelProxy = TestsHelper.loadTestModel();
        
        EObjectProxyCollection collection = testModelProxy.find();
        assertEquals(788, collection.size());

        for(EObjectProxy eObjectProxy : collection) {
            assertNotNull(eObjectProxy.getEObject());
        }
    }
    
    @Override
    @Test
    public void find_Selector() {
        super.find_Selector();
        
        EObjectProxy testModelProxy = TestsHelper.loadTestModel();
        
        EObjectProxyCollection collection = testModelProxy.find("garbage");
        assertEquals(0, collection.size());

        collection = testModelProxy.find("*");
        assertEquals(788, collection.size());
        
        collection = testModelProxy.find("concepts");
        assertEquals(298, collection.size());

        collection = testModelProxy.find("elements");
        assertEquals(120, collection.size());

        collection = testModelProxy.find("relations");
        assertEquals(178, collection.size());

        collection = testModelProxy.find("views");
        assertEquals(17, collection.size());

        collection = testModelProxy.find("#66a2171b");
        assertEquals(1, collection.size());
        assertEquals("66a2171b", collection.get(0).getId());

        collection = testModelProxy.find(".Business");
        assertEquals(2, collection.size());
        
        collection = testModelProxy.find("Folder.Business");
        assertEquals(2, collection.size());
        
        collection = testModelProxy.find("BusinessRole");
        assertEquals(20, collection.size());
    }

    @Override
    @Test
    public void attr_Get() {
        super.attr_Get();
        
        assertEquals("doc", actualTestProxy.attr(IModelConstants.PURPOSE));
    }

    @Override
    @Test
    public void attr_Set() {
        super.attr_Set();
        actualTestProxy.attr(IModelConstants.PURPOSE, "p");
        assertEquals("p", ((ArchimateModelProxy)testProxy).getPurpose());
    }
    
    @Test
    public void setPurpose() {
        actualTestProxy.setPurpose("purpose");
        assertEquals("purpose", actualTestProxy.getPurpose());
    }

    @Test
    public void load() {
        assertSame(testEObject, actualTestProxy.getEObject());
        
        ArchimateModelProxy proxy = actualTestProxy.load(TestData.TEST_MODEL_FILE_ARCHISURANCE.getAbsolutePath());
        assertSame(actualTestProxy, proxy);
        assertNotSame(testEObject, proxy.getEObject());
    }    

    @Test
    public void create() {
        assertSame(testEObject, actualTestProxy.getEObject());
        
        ArchimateModelProxy proxy = actualTestProxy.create("Test");
        assertSame(actualTestProxy, proxy);
        assertNotSame(testEObject, proxy.getEObject());
        
        assertEquals("Test", proxy.getName());
    }
    
    @Test
    public void copy() {
        assertSame(testEObject, actualTestProxy.getEObject());
        
        ArchimateModelProxy proxy = actualTestProxy.copy();
        assertSame(actualTestProxy.getEObject(), proxy.getEObject());
        assertEquals(actualTestProxy, proxy);
    }
    
    @Test
    public void save() throws IOException {
        File file = File.createTempFile("~temp", ".archimate");
        file.deleteOnExit();
        
        actualTestProxy.getEObject().setAdapter(IArchiveManager.class, IArchiveManager.FACTORY.createArchiveManager(actualTestProxy.getEObject()));
        actualTestProxy.save(file.getAbsolutePath());
        
        assertTrue(file.exists());
        assertEquals(133, file.length());
    }
   
    @Test
    public void addElement() {
        ArchimateElementProxy proxy = actualTestProxy.addElement("BusinessActor", "Fido");
        assertNotNull(proxy);
        
        IArchimateElement element = proxy.getEObject();
        assertEquals("Fido", element.getName());
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessActor(), element.eClass());
    }
    
    @Test(expected = ArchiScriptException.class)
    public void addElement_Bogus() {
        actualTestProxy.addElement("AccessRelationship", "Fido");
    }

    @Test
    public void addRelationship() {
        ArchimateElementProxy source = actualTestProxy.addElement("BusinessActor", "Fido");
        ArchimateElementProxy target = actualTestProxy.addElement("BusinessRole", "Role");
        
        ArchimateRelationshipProxy proxy = actualTestProxy.addRelationship("AssignmentRelationship", "Fido", source, target);
        assertNotNull(proxy);
        
        IArchimateRelationship relation = proxy.getEObject();
        assertEquals("Fido", relation.getName());
        assertEquals(IArchimatePackage.eINSTANCE.getAssignmentRelationship(), relation.eClass());
    }
    
    @Test(expected = ArchiScriptException.class)
    public void addRelationship_Bogus() {
        ArchimateElementProxy source = actualTestProxy.addElement("BusinessActor", "Fido");
        ArchimateElementProxy target = actualTestProxy.addElement("BusinessRole", "Role");
        actualTestProxy.addRelationship("BusinessActor", "Fido", source, target);
    }
    
    @Test(expected = ArchiScriptException.class)
    public void addRelationship_BogusType() {
        ArchimateElementProxy source = actualTestProxy.addElement("BusinessActor", "Fido");
        ArchimateElementProxy target = actualTestProxy.addElement("BusinessRole", "Role");
        actualTestProxy.addRelationship("AccessRelationship", "Fido", source, target);
    }
}