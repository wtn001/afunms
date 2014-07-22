package com.afunms.vmware.vim25.mgr;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class VMMgrTest {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(VMMgrTest.class);

	String url = "https://10.10.152.14/sdk";
	String username = "administrator";
	String password = "123456";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	// @Test
	public void testCreateVM() {
		//��� ���Գɹ�
		String dcId = "datacenter-2";
		String haId = "domain-c73";
		String rpId = "resgroup-131";
		// String hoId = "host-71";

		String name = "name11";
		String descr = "descr";

		String guestId = "centosGuest";
		int numCpus = 1;
		int numCores = 2;
		long memoryMB = 2048;
		String dsId = "datastore-72";
		long diskSizeMB = 10240;

		LOGGER.info(VMMgr.createVM(url, username, password, dcId, haId, rpId,
				name, descr, guestId, numCpus, numCores, memoryMB, dsId,
				diskSizeMB));
	}

	 //@Test
	public void testCloneVM() {
		//��¡ ���Գɹ�
		String dcId = "datacenter-2";
		String vmId = "vm-189";
		// ���cloneNameΪ��,��������´���
		// Fault
		// {http://schemas.xmlsoap.org/soap/envelope/}Server.generalException
		// Message ������Ч�򳬳������������ַ�����
		String cloneName = "testname2";
		LOGGER.info(VMMgr.cloneVM(url, username, password, dcId, vmId,cloneName));
	}

	// @Test
	public void testReconfigVM() {
		// �޸� ���Գɹ�
		String vmId = "vm-210";

		String dcId = "datacenter-2";

		int numCpus = 2;
		int numCores = 2;
		long memoryMB = 6144;

		long diskSizeMB = 10240;

		LOGGER.info(VMMgr.reconfigVM(url, username, password, dcId, vmId,
				numCpus, numCores, memoryMB, diskSizeMB));
	}

	// @Test
	public void testDestroyVM() {
		//ɾ�� ���Գɹ�
		String vmId = "vm-444444";
		LOGGER.info(VMMgr.destroyVM(url, username, password, vmId));
	}

	// @Test
	public void testVmOperation() {
		// FIXME
		fail("Not yet implemented");
	}

	 @Test
	public void testGetVMSummary() {
		String vmId = "vm-28";
		LOGGER.info(VMMgr.getSummary(url, username, password, vmId));
		fail("Not yet implemented");
	}

	// @Test
	public void testGetVMPerformance() {
		// FIXME
		fail("Not yet implemented");
	}

}
