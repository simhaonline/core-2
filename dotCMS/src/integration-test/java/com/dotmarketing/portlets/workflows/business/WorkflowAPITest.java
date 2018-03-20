package com.dotmarketing.portlets.workflows.business;

import com.dotcms.IntegrationTestBase;
import com.dotcms.contenttype.business.ContentTypeAPIImpl;
import com.dotcms.contenttype.business.FieldAPI;
import com.dotcms.contenttype.model.field.Field;
import com.dotcms.contenttype.model.field.ImmutableTextField;
import com.dotcms.contenttype.model.type.BaseContentType;
import com.dotcms.contenttype.model.type.ContentType;
import com.dotcms.contenttype.model.type.ContentTypeBuilder;
import com.dotcms.contenttype.transform.contenttype.StructureTransformer;
import com.dotcms.util.IntegrationTestInitService;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Permission;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.PermissionAPI;
import com.dotmarketing.business.Role;
import com.dotmarketing.business.RoleAPI;
import com.dotmarketing.exception.AlreadyExistException;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.business.HostAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.model.ContentletDependencies;
import com.dotmarketing.portlets.folders.business.FolderAPI;
import com.dotmarketing.portlets.structure.model.ContentletRelationships;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.portlets.workflows.actionlet.ArchiveContentActionlet;
import com.dotmarketing.portlets.workflows.actionlet.CheckinContentActionlet;
import com.dotmarketing.portlets.workflows.actionlet.DeleteContentActionlet;
import com.dotmarketing.portlets.workflows.actionlet.PublishContentActionlet;
import com.dotmarketing.portlets.workflows.actionlet.ResetTaskActionlet;
import com.dotmarketing.portlets.workflows.actionlet.SaveContentActionlet;
import com.dotmarketing.portlets.workflows.actionlet.SaveContentAsDraftActionlet;
import com.dotmarketing.portlets.workflows.actionlet.UnarchiveContentActionlet;
import com.dotmarketing.portlets.workflows.actionlet.UnpublishContentActionlet;
import com.dotmarketing.portlets.workflows.model.*;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.model.User;
import com.liferay.util.StringPool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the workflowAPI
 */
public class WorkflowAPITest extends IntegrationTestBase {

    private static User user;
    private static Host defaultHost;
    protected static ContentTypeAPIImpl contentTypeAPI;
    protected static FieldAPI fieldAPI;
    protected static WorkflowAPI workflowAPI;
    protected static RoleAPI roleAPI;
    protected static PermissionAPI permissionAPI;
    protected static ContentletAPI contentletAPI;

    private static String contentTypeName;
    private static String contentTypeName2;
    private static String contentTypeName3;
    private static String workflowSchemeName1;
    private static String workflowSchemeName2;
    private static String workflowSchemeName3;
    private static String workflowSchemeName4;
    private static String workflowSchemeName5;

    private static ContentType contentType;
    private static Structure contentTypeStructure;

    private static ContentType contentType2;
    private static Structure contentTypeStructure2;

    private static ContentType contentType3;
    private static Structure contentTypeStructure3;

    private static ContentType contentType4;
    private static Structure contentTypeStructure4;

    /* Workflow Scheme 1 */
    private static WorkflowScheme workflowScheme1;

    private static WorkflowStep workflowScheme1Step1;
    private static String workflowScheme1Step1Name;
    private static WorkflowAction workflowScheme1Step1Action1;
    private static String workflowScheme1Step1ActionIntranetName;

    private static WorkflowStep workflowScheme1Step2;
    private static String workflowScheme1Step2Name;
    private static WorkflowAction workflowScheme1Step2Action1;
    private static String workflowScheme1Step2Action1Name;

    /* Workflow Scheme 2 */
    private static WorkflowScheme workflowScheme2;

    private static WorkflowStep workflowScheme2Step1;
    private static String workflowScheme2Step1Name;
    private static WorkflowAction workflowScheme2Step1Action1;
    private static String workflowScheme2Step1ActionReviewerName;

    private static WorkflowStep workflowScheme2Step2;
    private static String workflowScheme2Step2Name;
    private static WorkflowAction workflowScheme2Step2Action1;
    private static String workflowScheme2Step2Action1Name;

    /* Workflow Scheme 3 */
    private static WorkflowScheme workflowScheme3;

    private static WorkflowStep workflowScheme3Step1;

    private static String workflowScheme3Step1Name;

    private static WorkflowAction workflowScheme3Step1Action1;
    private static String workflowScheme3Step1ActionPublisherName;

    private static WorkflowStep workflowScheme3Step2;

    private static String workflowScheme3Step2Name;

    private static WorkflowAction workflowScheme3Step2Action1;
    private static String workflowScheme3Step2Action1Name;

    private static WorkflowAction workflowScheme3Step2Action2;
    private static String workflowScheme3Step2Action2Name;

    /* Workflow Scheme 4 */
    private static WorkflowScheme workflowScheme4;

    private static WorkflowStep workflowScheme4Step1;
    private static String workflowScheme4Step1Name;
    private static WorkflowAction workflowScheme4Step1ActionView;
    private static String workflowScheme4Step1ActionViewName;
    private static WorkflowAction workflowScheme4Step1ActionEdit;
    private static String workflowScheme4Step1ActionEditName;
    private static WorkflowAction workflowScheme4Step1ActionPublish;
    private static String workflowScheme4Step1ActionPublishName;
    private static WorkflowAction workflowScheme4Step1ActionEditPermissions;
    private static String workflowScheme4Step1ActionEditPermissionsName;
    private static WorkflowAction workflowScheme4Step1ActionContributor;
    private static String workflowScheme4Step1ActionContributorName;

    private static WorkflowStep workflowScheme4Step2;
    private static String workflowScheme4Step2Name;
    private static WorkflowAction workflowScheme4Step2ActionView;
    private static String workflowScheme4Step2ActionViewName;
    private static WorkflowAction workflowScheme4Step2ActionEdit;
    private static String workflowScheme4Step2ActionEditName;
    private static WorkflowAction workflowScheme4Step2ActionPublish;
    private static String workflowScheme4Step2ActionPublishName;
    private static WorkflowAction workflowScheme4Step2ActionEditPermissions;
    private static String workflowScheme4Step2ActionEditPermissionsName;
    private static WorkflowAction workflowScheme4Step2ActionReviewer;
    private static String workflowScheme4Step2ActionReviewerName;

    private static WorkflowStep workflowScheme4Step3;
    private static String workflowScheme4Step3Name;
    private static WorkflowAction workflowScheme4Step3ActionView;
    private static String workflowScheme4Step3ActionViewName;
    private static WorkflowAction workflowScheme4Step3ActionEdit;
    private static String workflowScheme4Step3ActionEditName;
    private static WorkflowAction workflowScheme4Step3ActionPublish;
    private static String workflowScheme4Step3ActionPublishName;
    private static WorkflowAction workflowScheme4Step3ActionEditPermissions;
    private static String workflowScheme4Step3ActionEditPermissionsName;
    private static WorkflowAction workflowScheme4Step3ActionPublisher;
    private static String workflowScheme4Step3ActionPublisherName;

    /* Workflow Scheme 5 */
    private static WorkflowScheme workflowScheme5;

    private static WorkflowStep workflowScheme5Step1;
    private static String workflowScheme5Step1Name;
    private static WorkflowAction workflowScheme5Step1Action1;
    private static String workflowScheme5Step1ActionPublishName;
    private static String workflowScheme5Step1Action1SubAction1Name;
    private static WorkflowActionClass workflowScheme5Step1Action1SubAction1;

    /* Roles */
    private static Role reviewer;
    private static Role contributor;
    private static Role publisher;
    private static Role intranet;
    private static Role anyWhoView;
    private static Role anyWhoEdit;
    private static Role anyWhoPublish;
    private static Role anyWhoEditPermissions;

    private static final String FIELD_NAME ="Title";
    private static final String FIELD_VAR_NAME ="title";

    private static final String DOCUMENT_MANAGEMENT_WORKFLOW_NAME="Document Management";
    private static final String EDITING_STEP_NAME="Editing";
    private static final String REVIEW_STEP_NAME="Review";
    private static final String LEGAL_APPROVAL_STEP_NAME="Legal Approval";
    private static final String PUBLISHED_STEP_NAME="Published";
    private static final String ARCHIVED_STEP_NAME="Archived";

    private static final String SAVE_AS_DRAFT_ACTION_NAME="Save as Draft";
    private static final String SEND_FOR_REVIEW_ACTION_NAME="Send for Review";
    private static final String RETURN_FOR_EDITS_ACTION_NAME="Return for Edits";
    private static final String SEND_TO_LEGAL_ACTION_NAME="Send to Legal";
    private static final String PUBLISH_ACTION_NAME="Publish";
    private static final String REPUBLISH_ACTION_NAME="Republish";
    private static final String UNPUBLISH_ACTION_NAME="Unpublish";
    private static final String ARCHIVE_ACTION_NAME="Archive";
    private static final String DELETE_ACTION_NAME="Full Delete";
    private static final String RESET_WORKFLOW_ACTION_NAME="Reset Workflow";

    private static final String SAVE_AS_DRAFT_SUBACTION="Save Draft content";
    private static final String PUBLISH_SUBACTION="Publish content";
    private static final String UNLOCK_SUBACTION="Unlock content";
    private static final String SAVE_CONTENT_SUBACTION="Save content";
    private static final String ARCHIVE_SUBACTION="Archive content";
    private static final String UNARCHIVE_SUBACTION="Unarchive content";
    private static final String UNPUBLISH_SUBACTION="Unpublish content";
    private static final String DELETE_SUBACTION="Unpublish content";
    private static final String RESET_WORKFLOW_SUBACTION="Reset Workflow";


    @BeforeClass
    public static void prepare() throws Exception {
        //Setting web app environment
        IntegrationTestInitService.getInstance().init();

        HostAPI hostAPI = APILocator.getHostAPI();

        //Setting the test user
        user = APILocator.getUserAPI().getSystemUser();
        defaultHost = hostAPI.findDefaultHost(user, false);
        contentTypeAPI = (ContentTypeAPIImpl) APILocator.getContentTypeAPI(user);
        fieldAPI = APILocator.getContentTypeFieldAPI();
        workflowAPI = APILocator.getWorkflowAPI();
        roleAPI = APILocator.getRoleAPI();
        permissionAPI = APILocator.getPermissionAPI();
        contentletAPI = APILocator.getContentletAPI();

        publisher = roleAPI.findRoleByName("Publisher / Legal", null);
        reviewer = roleAPI.findRoleByName("Reviewer", publisher);
        contributor = roleAPI.findRoleByName("Contributor", reviewer);
        intranet = roleAPI.findRoleByName("Intranet", null);
        anyWhoView = roleAPI.loadRoleByKey(RoleAPI.WORKFLOW_ANY_WHO_CAN_VIEW_ROLE_KEY);
        anyWhoEdit = roleAPI.loadRoleByKey(RoleAPI.WORKFLOW_ANY_WHO_CAN_EDIT_ROLE_KEY);
        anyWhoPublish = roleAPI.loadRoleByKey(RoleAPI.WORKFLOW_ANY_WHO_CAN_PUBLISH_ROLE_KEY);
        anyWhoEditPermissions = roleAPI.loadRoleByKey(RoleAPI.WORKFLOW_ANY_WHO_CAN_EDIT_PERMISSIONS_ROLE_KEY);

        long time = System.currentTimeMillis();
        contentTypeName = "WorkflowTesting_" + time;

        /**
         * Generate ContentType
         */
        contentType = insertContentType(contentTypeName, BaseContentType.CONTENT);
        contentTypeStructure = new StructureTransformer(ContentType.class.cast(contentType))
                .asStructure();

        /**
         * Generate workflow schemes
         */

        /*  Workflow 1 */
        workflowSchemeName1 = "WorkflowSchemeTest1" + time;
        workflowScheme1Step1Name = "WorkflowScheme1Step1_" + time;
        workflowScheme1Step1ActionIntranetName = "WorkflowScheme1Step1ActionIntranet_" + time;
        workflowScheme1Step2Name = "WorkflowScheme1Step2_" + time;
        workflowScheme1Step2Action1Name = "WorkflowScheme1Step2ActionReviewer_" + time;
        workflowScheme1 = addWorkflowScheme(workflowSchemeName1);

        /* Generate scheme steps */
        workflowScheme1Step1 = addWorkflowStep(workflowScheme1Step1Name, 1, false, false,
                workflowScheme1.getId());

        workflowScheme1Step2 = addWorkflowStep(workflowScheme1Step2Name, 2, true, false,
                workflowScheme1.getId());

        /* Generate actions */
        workflowScheme1Step2Action1 = addWorkflowAction(workflowScheme1Step2Action1Name, 2,
                workflowScheme1Step2.getId(), true, workflowScheme1Step2.getId(), reviewer, workflowScheme1.getId());

        workflowScheme1Step1Action1 = addWorkflowAction(workflowScheme1Step1ActionIntranetName, 1,
                workflowScheme1Step2.getId(), true, workflowScheme1Step1.getId(), intranet, workflowScheme1.getId());



        /* not Mandatory Workflows */
        workflowSchemeName2 = "WorkflowSchemeTest2_" + time;
        workflowScheme2Step1Name = "WorkflowScheme2Step1_" + time;
        workflowScheme2Step1ActionReviewerName = "WorkflowScheme2Step1ActionReviewer_" + time;
        workflowScheme2Step2Name = "WorkflowScheme2Step2_" + time;
        workflowScheme2Step2Action1Name = "WorkflowScheme2Step2ActionReviewer_" + time;
        workflowScheme2 = addWorkflowScheme(workflowSchemeName2);

        /* Generate scheme steps */
        workflowScheme2Step1 = addWorkflowStep(workflowScheme2Step1Name, 1, false, false,
                workflowScheme2.getId());

        workflowScheme2Step2 = addWorkflowStep(workflowScheme2Step2Name, 2, false, false,
                workflowScheme2.getId());

        /* Generate actions */
        workflowScheme2Step2Action1 = addWorkflowAction(workflowScheme2Step2Action1Name, 2,
                workflowScheme2Step2.getId(), true, workflowScheme2Step2.getId(), contributor,
                workflowScheme2.getId());

        workflowScheme2Step1Action1 = addWorkflowAction(workflowScheme2Step1ActionReviewerName, 1,
                workflowScheme2Step2.getId(), true, workflowScheme2Step1.getId(), reviewer,
                workflowScheme2.getId());



        /* not Mandatory Workflows */
        workflowSchemeName3 = "WorkflowSchemeTest3_" + time;
        workflowScheme3Step1Name = "WorkflowScheme3Step1_" + time;
        workflowScheme3Step1ActionPublisherName = "WorkflowScheme3Step1ActionPublisher_" + time;
        workflowScheme3Step2Name = "WorkflowScheme3Step2_" + time;
        workflowScheme3Step2Action1Name = "WorkflowScheme2Step2ActionReviewer_" + time;
        workflowScheme3Step2Action2Name = "WorkflowScheme2Step2ActionPublisher_" + time;
        workflowScheme3 = addWorkflowScheme(workflowSchemeName3);

        /* Generate scheme steps */
        workflowScheme3Step1 = addWorkflowStep(workflowScheme3Step1Name, 1, false, false,
                workflowScheme3.getId());

        workflowScheme3Step2 = addWorkflowStep(workflowScheme3Step2Name, 2, false, false,
                workflowScheme3.getId());

        /* Generate actions */
        workflowScheme3Step2Action1 = addWorkflowAction(workflowScheme3Step2Action1Name, 2,
                workflowScheme3Step2.getId(), true, workflowScheme3Step2.getId(), reviewer,
                workflowScheme3.getId());

        workflowScheme3Step2Action2 = addWorkflowAction(workflowScheme3Step2Action2Name, 3,
                workflowScheme3Step2.getId(), true, workflowScheme3Step2.getId(), contributor,
                workflowScheme3.getId());

        workflowScheme3Step1Action1 = addWorkflowAction(workflowScheme3Step1ActionPublisherName, 1,
                workflowScheme3Step2.getId(), true, workflowScheme3Step1.getId(), publisher,
                workflowScheme3.getId());

        //fourth Workflow Scheme
        contentTypeName2 = "WorkflowTesting2_" + time;
        /* Mandatory Workflow */
        workflowSchemeName4 = "WorkflowSchemeTest4" + time;
        workflowScheme4Step1Name = "WorkflowScheme4Step1_" + time;
        workflowScheme4Step1ActionViewName = "WorkflowScheme4Step1ActionView_" + time;
        workflowScheme4Step1ActionEditName = "WorkflowScheme4Step1ActionEdit_" + time;
        workflowScheme4Step1ActionPublishName = "WorkflowScheme4Step1ActionPublish_" + time;
        workflowScheme4Step1ActionEditPermissionsName = "WorkflowScheme4Step1ActionEditPermissions_" + time;
        workflowScheme4Step1ActionContributorName = "WorkflowScheme4Step1ActionContributor_" + time;

        workflowScheme4Step2Name = "WorkflowScheme4Step2_" + time;
        workflowScheme4Step2ActionViewName = "WorkflowScheme4Step2ActionView_" + time;
        workflowScheme4Step2ActionEditName = "WorkflowScheme4Step2ActionEdit_" + time;
        workflowScheme4Step2ActionPublishName = "WorkflowScheme4Step2ActionPublish_" + time;
        workflowScheme4Step2ActionEditPermissionsName = "WorkflowScheme4Step2ActionEditPermissions_" + time;
        workflowScheme4Step2ActionReviewerName = "WorkflowScheme4Step2ActionReviewer_" + time;

        workflowScheme4Step3Name = "WorkflowScheme4Step3_" + time;
        workflowScheme4Step3ActionViewName = "WorkflowScheme4Step3ActionView_" + time;
        workflowScheme4Step3ActionEditName = "WorkflowScheme4Step3ActionEdit_" + time;
        workflowScheme4Step3ActionPublishName = "WorkflowScheme4Step3ActionPublish_" + time;
        workflowScheme4Step3ActionEditPermissionsName = "WorkflowScheme4Step3ActionEditPermissions_" + time;
        workflowScheme4Step3ActionPublisherName = "WorkflowScheme4Step3ActionPublisher_" + time;

        /**
         * Generate ContentType 2
         */
        contentType2 = insertContentType(contentTypeName2, BaseContentType.CONTENT);
        contentTypeStructure2 = new StructureTransformer(ContentType.class.cast(contentType2))
                .asStructure();

        /**
         * Generate workflow schemes
         */
        workflowScheme4 = addWorkflowScheme(workflowSchemeName4);

        /* Generate scheme steps */
        workflowScheme4Step1 = addWorkflowStep(workflowScheme4Step1Name, 1, false, false,
                workflowScheme4.getId());

        workflowScheme4Step2 = addWorkflowStep(workflowScheme4Step2Name, 2, true, false,
                workflowScheme4.getId());

        workflowScheme4Step3 = addWorkflowStep(workflowScheme4Step3Name, 3, true, false,
                workflowScheme4.getId());

        /* Generate actions */
        workflowScheme4Step3ActionView = addWorkflowAction(workflowScheme4Step3ActionViewName, 1,
                workflowScheme4Step3.getId(), false, workflowScheme4Step3.getId(), anyWhoView,
                workflowScheme4.getId());
        workflowScheme4Step3ActionEdit = addWorkflowAction(workflowScheme4Step3ActionEditName, 2,
                workflowScheme4Step3.getId(), false, workflowScheme4Step3.getId(), anyWhoEdit,
                workflowScheme4.getId());
        workflowScheme4Step3ActionPublish = addWorkflowAction(workflowScheme4Step3ActionPublishName, 3,
                workflowScheme4Step3.getId(), false, workflowScheme4Step3.getId(), anyWhoPublish,
                workflowScheme4.getId());
        workflowScheme4Step3ActionEditPermissions = addWorkflowAction(workflowScheme4Step3ActionEditPermissionsName, 4,
                workflowScheme4Step3.getId(), false, workflowScheme4Step3.getId(),
                anyWhoEditPermissions, workflowScheme4.getId());
        workflowScheme4Step3ActionPublisher = addWorkflowAction(workflowScheme4Step3ActionPublisherName, 5,
                workflowScheme4Step3.getId(), false, workflowScheme4Step3.getId(), publisher,
                workflowScheme4.getId());

        workflowScheme4Step2ActionView = addWorkflowAction(workflowScheme4Step2ActionViewName, 1,
                workflowScheme4Step3.getId(), false, workflowScheme4Step2.getId(), anyWhoView,
                workflowScheme4.getId());
        workflowScheme4Step2ActionEdit = addWorkflowAction(workflowScheme4Step2ActionEditName, 2,
                workflowScheme4Step3.getId(), false, workflowScheme4Step2.getId(), anyWhoEdit,
                workflowScheme4.getId());
        workflowScheme4Step2ActionPublish = addWorkflowAction(workflowScheme4Step2ActionPublishName, 3,
                workflowScheme4Step3.getId(), false, workflowScheme4Step2.getId(), anyWhoPublish,
                workflowScheme4.getId());
        workflowScheme4Step2ActionEditPermissions = addWorkflowAction(workflowScheme4Step2ActionEditPermissionsName, 4,
                workflowScheme4Step3.getId(), false, workflowScheme4Step2.getId(),
                anyWhoEditPermissions, workflowScheme4.getId());
        workflowScheme4Step2ActionReviewer = addWorkflowAction(workflowScheme4Step2ActionReviewerName, 5,
                workflowScheme4Step3.getId(), false, workflowScheme4Step2.getId(), reviewer,
                workflowScheme4.getId());

        workflowScheme4Step1ActionView = addWorkflowAction(workflowScheme4Step1ActionViewName, 1,
                workflowScheme4Step2.getId(), false, workflowScheme4Step1.getId(), anyWhoView,
                workflowScheme4.getId());
        workflowScheme4Step1ActionEdit = addWorkflowAction(workflowScheme4Step1ActionEditName, 2,
                workflowScheme4Step2.getId(), false, workflowScheme4Step1.getId(), anyWhoEdit,
                workflowScheme4.getId());
        workflowScheme4Step1ActionPublish = addWorkflowAction(workflowScheme4Step1ActionPublishName, 3,
                workflowScheme4Step2.getId(), false, workflowScheme4Step1.getId(), anyWhoPublish,
                workflowScheme4.getId());
        workflowScheme4Step1ActionEditPermissions = addWorkflowAction(workflowScheme4Step1ActionEditPermissionsName, 4,
                workflowScheme4Step2.getId(), false, workflowScheme4Step1.getId(),
                anyWhoEditPermissions, workflowScheme4.getId());
        workflowScheme4Step1ActionContributor = addWorkflowAction(workflowScheme4Step1ActionContributorName, 5,
                workflowScheme4Step2.getId(), false, workflowScheme4Step1.getId(), contributor,
                workflowScheme4.getId());


        /**
         * Generate ContentType 3
         */
        contentTypeName3 = "WorkflowTesting3_" + time;
        contentType3 = insertContentType(contentTypeName3, BaseContentType.CONTENT);
        contentTypeStructure3 = new StructureTransformer(ContentType.class.cast(contentType3))
                .asStructure();


        /* Workflow */
        workflowSchemeName5 = "WorkflowSchemeTest5" + time;
        workflowScheme5Step1Name = "WorkflowScheme5Step1_" + time;
        workflowScheme5Step1ActionPublishName = "WorkflowScheme5Step1ActionPublish_" + time;
        workflowScheme5Step1Action1SubAction1Name="Publish content";

        workflowScheme5 = addWorkflowScheme(workflowSchemeName5);

        /* Generate scheme steps */
        workflowScheme5Step1 = addWorkflowStep(workflowScheme5Step1Name, 1, false, false,
                workflowScheme5.getId());

        workflowScheme5Step1Action1 = addWorkflowAction(workflowScheme5Step1ActionPublishName, 1,
                workflowScheme5Step1.getId(), true, workflowScheme5Step1.getId(), anyWhoView, workflowScheme5.getId());

        workflowScheme5Step1Action1SubAction1 = addSubActionClass(workflowScheme5Step1Action1SubAction1Name,
                workflowScheme5Step1Action1.getId(),
                com.dotmarketing.portlets.workflows.actionlet.PublishContentActionlet.class,1);


    }

    /**
     * This method test the saveSchemesForStruct method
     */
    @Test
    public void saveSchemesForStruct() throws DotDataException, DotSecurityException {

        List<WorkflowScheme> worflowSchemes = new ArrayList<>();

        /* Validate that the workflow scheme was created*/
        workflowScheme1 = workflowAPI.findSchemeByName(workflowSchemeName1);
        assertTrue(null != workflowScheme1 && UtilMethods.isSet(workflowScheme1.getId()));

        worflowSchemes.add(workflowScheme1);

        /* Validate that the workflow scheme was created*/
        workflowScheme2 = workflowAPI.findSchemeByName(workflowSchemeName2);
        assertTrue(null != workflowScheme2 && UtilMethods.isSet(workflowScheme2.getId()));

        worflowSchemes.add(workflowScheme2);

        /* Validate that the workflow scheme was created */
        workflowScheme3 = workflowAPI.findSchemeByName(workflowSchemeName3);
        assertTrue(null != workflowScheme3 && UtilMethods.isSet(workflowScheme3.getId()));

        worflowSchemes.add(workflowScheme3);

        /* Associate the schemas to the content type */
        workflowAPI.saveSchemesForStruct(contentTypeStructure, worflowSchemes);

        List<WorkflowScheme> contentTypeSchemes = workflowAPI
                .findSchemesForStruct(contentTypeStructure);
        assertTrue(contentTypeSchemes != null && contentTypeSchemes.size() == 3);

        /* Validate that the default scheme is not associated to the content tyepe*/
        WorkflowScheme defaultScheme = workflowAPI.findDefaultScheme();
        assertFalse(containsScheme(defaultScheme, contentTypeSchemes));
        assertTrue(containsScheme(workflowScheme1, contentTypeSchemes));
        assertTrue(containsScheme(workflowScheme2, contentTypeSchemes));
        assertTrue(containsScheme(workflowScheme3, contentTypeSchemes));

        /* Associate the default schema to the content type */
        worflowSchemes.add(defaultScheme);
        workflowAPI.saveSchemesForStruct(contentTypeStructure, worflowSchemes);

        /* validate that the schemes area associated */
        contentTypeSchemes = workflowAPI.findSchemesForStruct(contentTypeStructure);
        assertTrue(contentTypeSchemes != null && contentTypeSchemes.size() == 4);
        assertTrue(containsScheme(defaultScheme, contentTypeSchemes));
        assertTrue(containsScheme(workflowScheme1, contentTypeSchemes));
        assertTrue(containsScheme(workflowScheme2, contentTypeSchemes));
        assertTrue(containsScheme(workflowScheme3, contentTypeSchemes));
    }

    /**
     * This method test the findStepsByContentlet method
     */
    @Test
    public void findStepsByContentlet() throws DotDataException, DotSecurityException {
        Contentlet c1 = new Contentlet();
        Contentlet c2 = new Contentlet();
        try {
            List<WorkflowScheme> worflowSchemes = new ArrayList<>();
            worflowSchemes.add(workflowScheme1);
            worflowSchemes.add(workflowScheme2);
            worflowSchemes.add(workflowScheme3);

            /* Associate the schemas to the content type */
            workflowAPI.saveSchemesForStruct(contentTypeStructure, worflowSchemes);

            long time = System.currentTimeMillis();

            //create contentlets
            c1.setLanguageId(1);
            c1.setStringProperty(FIELD_VAR_NAME, "WorkflowContentTest1_" + time);
            c1.setContentTypeId(contentType.id());
            c1 = contentletAPI.checkin(c1, user, false);

            c2.setLanguageId(1);
            c2.setStringProperty(FIELD_VAR_NAME, "WorkflowContentTest2_" + time);
            c2.setContentTypeId(contentType.id());
            c2 = contentletAPI.checkin(c2, user, false);

            contentletAPI.isInodeIndexed(c1.getInode());
            contentletAPI.isInodeIndexed(c2.getInode());

            Contentlet c = APILocator.getContentletAPI().checkout(c2.getInode(), user, false);

            //set step action for content2
            c.setStringProperty("wfActionId", workflowScheme2Step1Action1.getId());
            c.setStringProperty("wfActionComments", "Test" + time);

            c2 = APILocator.getContentletAPI().checkin(c, user, false);

            //check steps available for content without step
            List<WorkflowStep> steps = workflowAPI.findStepsByContentlet(c1);
            assertTrue(steps.size() == 3);

            //get step for content with a selection action
            steps = workflowAPI.findStepsByContentlet(c2);
            assertTrue(steps.size() == 1);
            assertTrue(workflowScheme2Step2.getName().equals(steps.get(0).getName()));
        }finally {
            contentletAPI.archive(c1,user,false);
            contentletAPI.delete(c1,user,false);
            contentletAPI.archive(c2,user,false);
            contentletAPI.delete(c2,user,false);
        }

    }

    /**
     * This method test the findActions methods
     */
    @Test
    public void findActions() throws DotDataException, DotSecurityException {

        List<WorkflowStep> steps = workflowAPI.findSteps(workflowScheme3);
        assertNotNull(steps);
        assertEquals(2, steps.size());

        //check available actions for admin user
        List<WorkflowAction> actions = workflowAPI.findActions(steps, user);
        assertNotNull(actions);
        assertEquals(3, actions.size());

        //get a contributor users
        User contributorUser = roleAPI.findUsersForRole(contributor).get(0);
        assertTrue(null != contributorUser && UtilMethods.isSet(contributorUser.getUserId()));

        //Check valid action for restricted user
        actions = workflowAPI.findActions(steps, contributorUser);
        assertTrue(null != actions && actions.size() == 1);

        //Get a reviewer  user
        User reviewerUser = roleAPI.findUsersForRole(reviewer).get(0);
        assertTrue(null != contributorUser && UtilMethods.isSet(contributorUser.getUserId()));

        //check valid action for
        actions = workflowAPI.findActions(steps, reviewerUser);
        assertTrue(null != actions && actions.size() == 2);
    }

    /**
     * This method test the findTaskByContentlet method
     */
    @Test
    public void findTaskByContentlet() throws DotDataException, DotSecurityException {

        Contentlet c1 = new Contentlet();
        try {
            List<WorkflowScheme> worflowSchemes = new ArrayList<>();
            worflowSchemes.add(workflowScheme1);
            worflowSchemes.add(workflowScheme2);
            worflowSchemes.add(workflowScheme3);

            /* Associate the schemas to the content type */
            workflowAPI.saveSchemesForStruct(contentTypeStructure, worflowSchemes);

            long time = System.currentTimeMillis();

            //create contentlets
            c1.setLanguageId(1);
            c1.setStringProperty(FIELD_VAR_NAME, "WorkflowContentTest3_" + time);
            c1.setContentTypeId(contentType.id());
            c1 = contentletAPI.checkin(c1, user, false);

            contentletAPI.isInodeIndexed(c1.getInode());

            Contentlet c = contentletAPI.checkout(c1.getInode(), user, false);

            //set step action for content2
            c.setStringProperty("wfActionId", workflowScheme3Step1Action1.getId());
            c.setStringProperty("wfActionComments", "Test" + time);

            c1 = contentletAPI.checkin(c, user, false);

            //check steps available for content without step
            WorkflowTask task = workflowAPI.findTaskByContentlet(c1);
            assertNotNull(task);
            //task should be on the second step of the scheme 3
            assertTrue(workflowScheme3Step2.getId().equals(task.getStatus()));

        }finally {
            contentletAPI.archive(c1,user,false);
            contentletAPI.delete(c1,user,false);
        }
    }


    /**
     * Test the find findAvailableActions methods
     */
    @Test
    public void findAvailableActions() throws DotDataException, DotSecurityException {

        //Users
        final User joeContributor = APILocator.getUserAPI().loadUserById("dotcms.org.2789");
        final User janeReviewer = APILocator.getUserAPI().loadUserById("dotcms.org.2787");
        final User chrisPublisher = APILocator.getUserAPI().loadUserById("dotcms.org.2795");
        final User billIntranet = APILocator.getUserAPI().loadUserById("dotcms.org.2806");

        /*
        Need to do the test checking with different user the actions displayed. We need to specify
        the permission for Intranet, Reviewer, Contributor and Publisher to see if the action
        returned are the right ones
         */

        Contentlet testContentlet = new Contentlet();
        try {
            List<WorkflowScheme> worflowSchemes = new ArrayList<>();
            worflowSchemes.add(workflowScheme1);
            worflowSchemes.add(workflowScheme2);
            worflowSchemes.add(workflowScheme3);
            worflowSchemes.add(workflowScheme4);

            /* Associate the schemas to the content type */
            workflowAPI.saveSchemesForStruct(contentTypeStructure, worflowSchemes);

            long time = System.currentTimeMillis();

            //Create a test contentlet
            testContentlet.setLanguageId(1);
            testContentlet.setStringProperty(FIELD_VAR_NAME, "WorkflowContentTest_" + time);
            testContentlet.setContentTypeId(contentType.id());
            testContentlet.setHost(defaultHost.getIdentifier());
            testContentlet = contentletAPI.checkin(testContentlet, user, false);

            contentletAPI.isInodeIndexed(testContentlet.getInode());

            //Adding permissions to the just created contentlet
            List<Permission> permissions = new ArrayList<>();
            Permission p1 = new Permission(
                    testContentlet.getPermissionId(),
                    APILocator.getRoleAPI().getUserRole(billIntranet).getId(),
                    (PermissionAPI.PERMISSION_READ | PermissionAPI.PERMISSION_EDIT),
                    true);
            Permission p2 = new Permission(
                    testContentlet.getPermissionId(),
                    APILocator.getRoleAPI().getUserRole(janeReviewer).getId(),
                    (PermissionAPI.PERMISSION_READ | PermissionAPI.PERMISSION_EDIT
                            | PermissionAPI.PERMISSION_WRITE),
                    true);
            Permission p3 = new Permission(
                    testContentlet.getPermissionId(),
                    APILocator.getRoleAPI().getUserRole(chrisPublisher).getId(),
                    (PermissionAPI.PERMISSION_READ | PermissionAPI.PERMISSION_EDIT
                            | PermissionAPI.PERMISSION_WRITE
                            | PermissionAPI.PERMISSION_PUBLISH),
                    true);
            Permission p4 = new Permission(
                    testContentlet.getPermissionId(),
                    APILocator.getRoleAPI().getUserRole(joeContributor).getId(),
                    (PermissionAPI.PERMISSION_READ | PermissionAPI.PERMISSION_EDIT
                            | PermissionAPI.PERMISSION_WRITE
                            | PermissionAPI.PERMISSION_PUBLISH
                            | PermissionAPI.PERMISSION_EDIT_PERMISSIONS),
                    true);
            permissions.add(p1);
            permissions.add(p2);
            permissions.add(p3);
            permissions.add(p4);

            APILocator.getPermissionAPI().save(permissions, testContentlet, user, false);

            //Validate the saved permissions
            List<Permission> foundPermissions = APILocator.getPermissionAPI()
                    .getPermissions(testContentlet);
            assertNotNull(foundPermissions);
            assertFalse(foundPermissions.isEmpty());

            /*
            Verify we are using/searching correctly with the special roles:
                - Any who can View
                - Any who can Edit
                - Any who can Publish
                - Any who can Edit Permission
             */
            List<WorkflowAction> foundActions = APILocator.getWorkflowAPI()
                    .findAvailableActions(testContentlet, billIntranet);
            assertNotNull(foundActions);
            assertFalse(foundActions.isEmpty());
            assertEquals(foundActions.size(), 4);

            foundActions = APILocator.getWorkflowAPI()
                    .findAvailableActions(testContentlet, janeReviewer);
            assertNotNull(foundActions);
            assertFalse(foundActions.isEmpty());
            assertEquals(foundActions.size(), 4);

            foundActions = APILocator.getWorkflowAPI()
                    .findAvailableActions(testContentlet, chrisPublisher);
            assertNotNull(foundActions);
            assertFalse(foundActions.isEmpty());
            assertEquals(foundActions.size(), 6);

            foundActions = APILocator.getWorkflowAPI()
                    .findAvailableActions(testContentlet, joeContributor);
            assertNotNull(foundActions);
            assertFalse(foundActions.isEmpty());
            assertEquals(foundActions.size(), 5);

        } finally {
            contentletAPI.archive(testContentlet, user, false);
            contentletAPI.delete(testContentlet, user, false);
        }

    }

    /**
     * Test the find findActionRespectingPermissions methods
     */
    @Test
    public void findActionRespectingPermissions() throws DotDataException, DotSecurityException {

        //Users
        final User billIntranet = APILocator.getUserAPI().loadUserById("dotcms.org.2806");
        final User chrisPublisher = APILocator.getUserAPI().loadUserById("dotcms.org.2795");


        Contentlet testContentlet = new Contentlet();
        try {

            //Set Workflow on contentType3
            List<WorkflowScheme> worflowSchemes = new ArrayList<>();
            worflowSchemes.add(workflowScheme5);

            /* Associate the schemas to the content type */
            workflowAPI.saveSchemesForStruct(contentTypeStructure3, worflowSchemes);

            long time = System.currentTimeMillis();
            testContentlet.setLanguageId(1);
            testContentlet.setStringProperty(FIELD_VAR_NAME, "Workflow5ContentTest_" + time);
            testContentlet.setContentTypeId(contentType3.id());
            testContentlet.setHost(defaultHost.getIdentifier());
            testContentlet = contentletAPI.checkin(testContentlet, APILocator.getPermissionAPI().getPermissions(testContentlet, false, true), user, false);

            contentletAPI.isInodeIndexed(testContentlet.getInode());

            //Adding permissions to limited user on the contentType3
            List<Permission> permissions = new ArrayList<>();
            Permission p1 = new Permission(
                    testContentlet.getPermissionId(),
                    APILocator.getRoleAPI().getUserRole(chrisPublisher).getId(),
                    (PermissionAPI.PERMISSION_READ | PermissionAPI.PERMISSION_EDIT
                            | PermissionAPI.PERMISSION_WRITE
                            | PermissionAPI.PERMISSION_PUBLISH
                            | PermissionAPI.PERMISSION_EDIT_PERMISSIONS),
                    true);
            permissions.add(p1);

            APILocator.getPermissionAPI().save(permissions, testContentlet, user, false);


            //Validate the saved permissions
            List<Permission> foundContentletPermissions = APILocator.getPermissionAPI()
                    .getPermissions(testContentlet);
            assertNotNull(foundContentletPermissions);
            assertFalse(foundContentletPermissions.isEmpty());

            WorkflowAction action = APILocator.getWorkflowAPI().findActionRespectingPermissions(workflowScheme5Step1Action1.getId(),testContentlet,chrisPublisher);
            assertNotNull(action);
            assertEquals(action.getName(), workflowScheme5Step1Action1.getName());

            //This should throw a DotSecurityException
            try {
                action = APILocator.getWorkflowAPI()
                        .findActionRespectingPermissions(workflowScheme5Step1Action1.getId(),
                                testContentlet, billIntranet);
            }catch (Exception e){
                assertTrue(e instanceof DotSecurityException);
            }


            action = APILocator.getWorkflowAPI().findActionRespectingPermissions(workflowScheme5Step1Action1.getId(),workflowScheme5Step1.getId(),testContentlet,chrisPublisher);
            assertNotNull(action);
            assertEquals(action.getName(), workflowScheme5Step1Action1.getName());

            //This should throw a DotSecurityException
            try {
                action = APILocator.getWorkflowAPI()
                        .findActionRespectingPermissions(workflowScheme5Step1Action1.getId(),
                                workflowScheme5Step1.getId(), testContentlet, billIntranet);
            }catch (Exception e){
                assertTrue(e instanceof DotSecurityException);
            }

        } finally {
            contentletAPI.archive(testContentlet, user, false);
            contentletAPI.delete(testContentlet, user, false);
        }

    }

    /**
     * Test the find findAction methods
     */
    @Test
    public void findAction() throws DotDataException, DotSecurityException {

        //Users
        final User billIntranet = APILocator.getUserAPI().loadUserById("dotcms.org.2806");
        final User chrisPublisher = APILocator.getUserAPI().loadUserById("dotcms.org.2795");

        Contentlet testContentlet = new Contentlet();
        try {

            //Set Workflow on contentType3
            List<WorkflowScheme> worflowSchemes = new ArrayList<>();
            worflowSchemes.add(workflowScheme5);

            /* Associate the schemas to the content type */
            workflowAPI.saveSchemesForStruct(contentTypeStructure3, worflowSchemes);

            long time = System.currentTimeMillis();
            testContentlet.setLanguageId(1);
            testContentlet.setStringProperty(FIELD_VAR_NAME, "Workflow5ContentTest_" + time);
            testContentlet.setContentTypeId(contentType3.id());
            testContentlet.setHost(defaultHost.getIdentifier());
            testContentlet = contentletAPI.checkin(testContentlet, APILocator.getPermissionAPI().getPermissions(testContentlet, false, true), user, false);

            contentletAPI.isInodeIndexed(testContentlet.getInode());

            //Adding permissions to limited user on the contentType3
            List<Permission> permissions = new ArrayList<>();
            Permission p1 = new Permission(
                    testContentlet.getPermissionId(),
                    APILocator.getRoleAPI().getUserRole(chrisPublisher).getId(),
                    (PermissionAPI.PERMISSION_READ | PermissionAPI.PERMISSION_EDIT
                            | PermissionAPI.PERMISSION_WRITE
                            | PermissionAPI.PERMISSION_PUBLISH
                            | PermissionAPI.PERMISSION_EDIT_PERMISSIONS),
                    true);
            permissions.add(p1);

            APILocator.getPermissionAPI().save(permissions, testContentlet, user, false);


            //Validate the saved permissions
            List<Permission> foundContentletPermissions = APILocator.getPermissionAPI()
                    .getPermissions(testContentlet);
            assertNotNull(foundContentletPermissions);
            assertFalse(foundContentletPermissions.isEmpty());

            WorkflowAction action = APILocator.getWorkflowAPI().findAction(workflowScheme5Step1Action1.getId(),
                    workflowScheme5Step1.getId(),chrisPublisher);
            assertNotNull(action);
            assertEquals(action.getName(), workflowScheme5Step1Action1.getName());

            action = APILocator.getWorkflowAPI().findAction(workflowScheme5Step1Action1.getId(),
                    workflowScheme5Step1.getId(),chrisPublisher);
            assertNotNull(action);
            assertEquals(action.getName(), workflowScheme5Step1Action1.getName());

        } finally {
            contentletAPI.archive(testContentlet, user, false);
            contentletAPI.delete(testContentlet, user, false);
        }

    }

    /**
     * This Test validate that a workflow step could not be deleted if depends of another step or
     * has a contentlet related
     * @throws DotDataException
     * @throws IOException
     * @throws DotSecurityException
     */
    @Test
    public void issue5197() throws DotDataException, IOException, DotSecurityException{
        WorkflowScheme ws = null;
        WorkflowStep step1 = null;
        WorkflowStep step2 = null;
        WorkflowAction action1 = null;
        WorkflowAction action2 = null;
        ContentType st = null;
        Contentlet contentlet1 = null;

        try {
            final User adminUser = APILocator.getUserAPI()
                    .loadByUserByEmail("admin@dotcms.com", user, false);
            Role role = roleAPI.getUserRole(adminUser);

            final User anonymousUser = APILocator.getUserAPI().getAnonymousUser();
            final Role anonymousRole = roleAPI.getUserRole(anonymousUser);

		    /*
		     * Create workflow scheme
		     */
            String schemeName = "issue5197-" + UtilMethods.dateToHTMLDate(new Date(), "MM-dd-yyyy-HHmmss");
            addWorkflowScheme(schemeName);

            ws = workflowAPI.findSchemeByName(schemeName);
            assertTrue(UtilMethods.isSet(ws));

            /*
		     * Create scheme step1
		     */
            addWorkflowStep("Edit", 1, false, false, ws.getId());

            List<WorkflowStep> steps = workflowAPI.findSteps(ws);
            assertTrue(steps.size() == 1);
            step1 = steps.get(0);

            /*
		     * Create scheme step2
		     */
            addWorkflowStep("Publish", 2, true, false, ws.getId());
            steps = workflowAPI.findSteps(ws);
            assertTrue(steps.size() == 2);
            step2 = steps.get(1);

            /*
		     * Add action to scheme step1
		     */
            addWorkflowAction("Edit", 1,
                    step2.getId(), false, step1.getId(), anonymousRole,
                    ws.getId());

            final List<WorkflowAction> actions1 = workflowAPI.findActions(step1, user);
            assertTrue(actions1.size() == 1);
            action1 = actions1.get(0);

		    /*
		     * Add action to scheme step2
		     */
            addWorkflowAction("Publish", 1,
                    step2.getId(), false, step2.getId(), anonymousRole,
                    ws.getId());

            final List<WorkflowAction> actions2 = workflowAPI.findActions(step2, user);
            assertTrue(actions2.size() == 1);
            action2 = actions2.get(0);

		    /*
		     * Create structure and add workflow scheme
		     */
            st = insertContentType("Issue5197Structure",BaseContentType.CONTENT);
            final Structure contentTypeSt = new StructureTransformer(ContentType.class.cast(st))
                    .asStructure();
            Permission p = new Permission();
            p.setInode(st.getPermissionId());
            p.setRoleId(roleAPI.loadCMSAnonymousRole().getId());
            p.setPermission(PermissionAPI.PERMISSION_READ);
            permissionAPI.save(p, st, user, true);

            p = new Permission();
            p.setInode(st.getPermissionId());
            p.setRoleId(roleAPI.loadCMSAnonymousRole().getId());
            p.setPermission(PermissionAPI.PERMISSION_EDIT);
            permissionAPI.save(p, st, user, true);

            p = new Permission();
            p.setInode(st.getPermissionId());
            p.setRoleId(roleAPI.loadCMSAnonymousRole().getId());
            p.setPermission(PermissionAPI.PERMISSION_PUBLISH);
            permissionAPI.save(p, st, user, true);


            List<WorkflowScheme> schemes = new ArrayList<>();
            schemes.add(ws);
            workflowAPI.saveSchemesForStruct(contentTypeSt, schemes);

            /*
		     * Create test content and set it up in scheme step
		     */
            contentlet1 = new Contentlet();
            contentlet1.setContentTypeId(st.id());
            contentlet1.setHost(defaultHost.getIdentifier());
            contentlet1.setLanguageId(APILocator.getLanguageAPI().getDefaultLanguage().getId());
            contentlet1.setStringProperty("title",
                    "test5197-1" + UtilMethods.dateToHTMLDate(new Date(), "MM-dd-yyyy-HHmmss"));

            contentlet1 = contentletAPI.checkin(contentlet1, user, false);
            if (permissionAPI.doesUserHavePermission(contentlet1, PermissionAPI.PERMISSION_PUBLISH,user))
                APILocator.getVersionableAPI().setLive(contentlet1);

		    /*
		     * Test that delete is not possible for step2
		     * while has associated step or content
		     */
            contentlet1.setStringProperty("wfActionId", action1.getId());
            contentlet1.setStringProperty("wfActionComments", "step1");
            contentlet1.setStringProperty("wfActionAssign", role.getId());
            workflowAPI.fireWorkflowNoCheckin(contentlet1, user);

            contentlet1.setStringProperty("wfActionId", action2.getId());
            contentlet1.setStringProperty("wfActionComments", "step2");
            contentlet1.setStringProperty("wfActionAssign", role.getId());
            workflowAPI.fireWorkflowNoCheckin(contentlet1, user);

            WorkflowStep currentStep = workflowAPI.findStepByContentlet(contentlet1);
            assertNotNull(currentStep);
            assertTrue(currentStep.getId().equals(step2.getId()));

		    /*
		     * Validate that step2 could not be deleted
		     */
            try {
                workflowAPI.deleteStep(step2, user);
            } catch (Exception e) {
			/*
			 * Should enter here with this exception
			 * </br> <b> Step : 'Publish' is being referenced by </b> </br></br> Step : 'Edit' ->  Action : 'Edit' </br></br>
			 */
            }
            assertTrue(UtilMethods.isSet(workflowAPI.findStep(step2.getId())));
		    /*
		     * Validate correct deletion of step1
		     */
            workflowAPI.deleteStep(step1, user);

		    /*
		     * Validate that the step 1 was deleted from the scheme
		     */
            steps = workflowAPI.findSteps(ws);
            assertTrue(steps.size() == 1);
            assertTrue(steps.get(0).getId().equals(step2.getId()));

		    /*
		     * Validate that step2 could not be deleted
		     */
            try {
                workflowAPI.deleteStep(step2, user);
            } catch (Exception e) {
			/*
			 * Should enter here with this exception
			 * </br> <b> Step : 'Publish' is being referenced by: X Contentlet(s) </br></br>
			 */
            }
            currentStep = workflowAPI.findStepByContentlet(contentlet1);
            assertNotNull(currentStep);
            assertTrue(currentStep.getId().equals(step2.getId()));

		    /*
		     * Validate that step2 is not deleted
		     */
            steps = workflowAPI.findSteps(ws);
            assertTrue(steps.size() == 1);
            assertTrue(steps.get(0).getId().equals(step2.getId()));

        }finally {
		    /*
		     * Clean test
		     */
            contentTypeAPI.delete(st);
            workflowAPI.deleteStep(step2, user);
            workflowAPI.deleteScheme(ws, user);
        }
    }

    /**
     * This test validate a contentlet workflow flow through different steps with different users
     * simulating the Document Management workflow
     */
    @Test
    public void validatingDocumentManagementWorkflow()
            throws DotDataException, IOException, DotSecurityException {
        Contentlet contentlet1 = null;
        try {
            final User joeContributor = APILocator.getUserAPI().loadUserById("dotcms.org.2789");
            final User janeReviewer = APILocator.getUserAPI().loadUserById("dotcms.org.2787");
            final User chrisPublisher = APILocator.getUserAPI().loadUserById("dotcms.org.2795");

            final int editPermission =
                    PermissionAPI.PERMISSION_READ + PermissionAPI.PERMISSION_EDIT;
            final int publishPermission = editPermission + PermissionAPI.PERMISSION_PUBLISH;

            contentType4 = insertContentType(
                    "ValidatingDMWf" + UtilMethods.dateToHTMLDate(new Date(), "MM-dd-yyyy-HHmmss"),
                    BaseContentType.CONTENT);
            contentTypeStructure4 = new StructureTransformer(ContentType.class.cast(contentType4))
                    .asStructure();

            Permission p = new Permission(contentType4.getPermissionId(), contributor.getId(),
                    editPermission, true);
            permissionAPI.save(p, contentType4, user, true);

            p = new Permission(Contentlet.class.getCanonicalName(), contentType4.getPermissionId(),
                    contributor.getId(), editPermission, true);
            permissionAPI.save(p, contentType4, user, true);

            p = new Permission(contentType4.getPermissionId(), publisher.getId(), publishPermission,
                    true);
            permissionAPI.save(p, contentType4, user, true);

            p = new Permission(Contentlet.class.getCanonicalName(), contentType4.getPermissionId(),
                    publisher.getId(), publishPermission, true);
            permissionAPI.save(p, contentType4, user, true);

            //get the Document Management workflow scheme
            WorkflowScheme ws = createDocumentManagentReplica(
                    DOCUMENT_MANAGEMENT_WORKFLOW_NAME + UtilMethods
                            .dateToHTMLDate(new Date(), "MM-dd-yyyy-HHmmss"));

            List<String> schemes = new ArrayList<>();
            schemes.add(ws.getId());
            workflowAPI.saveSchemeIdsForContentType(contentType4, schemes);

            /*
             * Create test content and set it up in scheme step
		     */
            contentlet1 = new Contentlet();
            contentlet1.setContentTypeId(contentType4.id());
            contentlet1.setLanguageId(APILocator.getLanguageAPI().getDefaultLanguage().getId());
            contentlet1.setStringProperty(FIELD_VAR_NAME,
                    "testDocumentManagement-1" + UtilMethods
                            .dateToHTMLDate(new Date(), "MM-dd-yyyy-HHmmss"));

            List<WorkflowAction> actions = workflowAPI
                    .findAvailableActions(contentlet1, joeContributor);
            if (actions.isEmpty() || actions.size() != 1) {
                assertTrue("Incorrect number of actions available", false);
            }
            if (!SAVE_AS_DRAFT_ACTION_NAME.equals(actions.get(0).getName())) {
                assertTrue("Wromg action available", false);
            }
            final WorkflowAction saveAsDraft = actions.get(0);

            //As Contributor - Save as Draft
            ContentletRelationships contentletRelationships = APILocator.getContentletAPI()
                    .getAllRelationships(contentlet1);
            contentlet1 = APILocator.getWorkflowAPI().fireContentWorkflow(contentlet1,
                    new ContentletDependencies.Builder().respectAnonymousPermissions(Boolean.FALSE)
                            .modUser(joeContributor)
                            .relationships(contentletRelationships)
                            .workflowActionId(saveAsDraft.getId()) //Save as a draft
                            .workflowActionComments(StringPool.BLANK)
                            .workflowAssignKey(StringPool.BLANK)
                            .categories(Collections.emptyList())
                            .generateSystemEvent(Boolean.FALSE).build());

            contentletAPI.isInodeIndexed(contentlet1.getInode());
            assertTrue("Contentlet is on the wrong Workflow Step", EDITING_STEP_NAME
                    .equals(workflowAPI.findStepByContentlet(contentlet1).getName()));

            //As Contributor - Send for Review
            actions = workflowAPI.findAvailableActions(contentlet1, joeContributor);
            if (actions.isEmpty() || actions.size() != 1) {
                assertTrue("Incorrect number of actions available", false);
            }

            if (!SEND_FOR_REVIEW_ACTION_NAME.equals(actions.get(0).getName())) {
                assertTrue("Wrong action available", false);
            }

            final WorkflowAction sendForReview = actions.get(0);
            contentlet1 = APILocator.getWorkflowAPI().fireContentWorkflow(contentlet1,
                    new ContentletDependencies.Builder()
                            .respectAnonymousPermissions(Boolean.FALSE)
                            .modUser(joeContributor)
                            .relationships(contentletRelationships)
                            .workflowActionId(sendForReview.getId()) //Send For Review
                            .workflowActionComments(StringPool.BLANK)
                            .workflowAssignKey(StringPool.BLANK)
                            .categories(Collections.emptyList())
                            .generateSystemEvent(Boolean.FALSE).build());

            assertTrue("Contentlet is on the wrong Workflow Step", REVIEW_STEP_NAME
                    .equals(workflowAPI.findStepByContentlet(contentlet1).getName()));

            //As Reviewer - return for editing
            actions = workflowAPI.findAvailableActions(contentlet1, janeReviewer);
            if (actions.isEmpty() || actions.size() != 2) {
                assertTrue("Incorrect number of actions available", false);
            }
            if (!RETURN_FOR_EDITS_ACTION_NAME.equals(actions.get(0).getName())) {
                assertTrue("Wrong action available", false);
            }

            if (!SEND_TO_LEGAL_ACTION_NAME.equals(actions.get(1).getName())) {
                assertTrue("Wrong action available", false);
            }

            final WorkflowAction returnForEdits = actions.get(0);

            contentlet1 = APILocator.getWorkflowAPI().fireContentWorkflow(contentlet1,
                    new ContentletDependencies.Builder().respectAnonymousPermissions(Boolean.FALSE)
                            .modUser(janeReviewer)
                            .relationships(contentletRelationships)
                            .workflowActionId(returnForEdits.getId()) //Send For Review
                            .workflowActionComments(StringPool.BLANK)
                            .workflowAssignKey(contributor.getId())
                            .categories(Collections.emptyList())
                            .generateSystemEvent(Boolean.FALSE).build());

            assertTrue("Contentlet is on the wrong Workflow Step", EDITING_STEP_NAME
                    .equals(workflowAPI.findStepByContentlet(contentlet1).getName()));
            // As contributor. lock for editing
            contentletAPI.lock(contentlet1, joeContributor, false);

            //As Contributor - save as Draft
            actions = workflowAPI.findAvailableActions(contentlet1, joeContributor);
            if (actions.isEmpty() || actions.size() != 2) {
                assertTrue("Incorrect number of actions available", false);
            }
            if (!SAVE_AS_DRAFT_ACTION_NAME.equals(actions.get(0).getName())) {
                assertTrue("Wrong action available", false);
            }
            if (!SEND_FOR_REVIEW_ACTION_NAME.equals(actions.get(1).getName())) {
                assertTrue("Wrong action available", false);
            }
            final String title2 = contentlet1.getStringProperty(FIELD_VAR_NAME) + "-2";
            contentlet1.setStringProperty(FIELD_VAR_NAME, title2);

            contentlet1 = APILocator.getWorkflowAPI().fireContentWorkflow(contentlet1,
                    new ContentletDependencies.Builder().respectAnonymousPermissions(Boolean.FALSE)
                            .modUser(joeContributor)
                            .relationships(contentletRelationships)
                            .workflowActionId(saveAsDraft.getId()) //Send For Review
                            .workflowActionComments(StringPool.BLANK)
                            .workflowAssignKey(StringPool.BLANK)
                            .categories(Collections.emptyList())
                            .generateSystemEvent(Boolean.FALSE).build());

            assertTrue("Contentlet is on the wrong Workflow Step", EDITING_STEP_NAME
                    .equals(workflowAPI.findStepByContentlet(contentlet1).getName()));

            //As Contributor - Send for Review
            actions = workflowAPI.findAvailableActions(contentlet1, joeContributor);
            if (actions.isEmpty() || actions.size() != 2) {
                assertTrue("Incorrect number of actions available", false);
            }
            if (!SAVE_AS_DRAFT_ACTION_NAME.equals(actions.get(0).getName())) {
                assertTrue("Wrong action available", false);
            }
            if (!SEND_FOR_REVIEW_ACTION_NAME.equals(actions.get(1).getName())) {
                assertTrue("Wrong action available", false);
            }

            contentlet1 = APILocator.getWorkflowAPI().fireContentWorkflow(contentlet1,
                    new ContentletDependencies.Builder().respectAnonymousPermissions(Boolean.FALSE)
                            .modUser(joeContributor)
                            .relationships(contentletRelationships)
                            .workflowActionId(sendForReview.getId()) //Send For Review
                            .workflowActionComments(StringPool.BLANK)
                            .workflowAssignKey(StringPool.BLANK)
                            .categories(Collections.emptyList())
                            .generateSystemEvent(Boolean.FALSE).build());

            assertTrue("Contentlet is on the wrong Workflow Step", REVIEW_STEP_NAME
                    .equals(workflowAPI.findStepByContentlet(contentlet1).getName()));

            //As reviewer - send to legal
            actions = workflowAPI.findAvailableActions(contentlet1, janeReviewer);
            if (actions.isEmpty() || actions.size() != 2) {
                assertTrue("Incorrect number of actions available", false);
            }
            if (!RETURN_FOR_EDITS_ACTION_NAME.equals(actions.get(0).getName())) {
                assertTrue("Wrong action available", false);
            }
            if (!SEND_TO_LEGAL_ACTION_NAME.equals(actions.get(1).getName())) {
                assertTrue("Wrong action available", false);
            }

            final WorkflowAction sendToLegal = actions.get(1);

            contentlet1 = APILocator.getWorkflowAPI().fireContentWorkflow(contentlet1,
                    new ContentletDependencies.Builder().respectAnonymousPermissions(Boolean.FALSE)
                            .modUser(janeReviewer)
                            .relationships(contentletRelationships)
                            .workflowActionId(sendToLegal.getId()) //Send to Legal
                            .workflowActionComments(StringPool.BLANK)
                            .workflowAssignKey(StringPool.BLANK)
                            .categories(Collections.emptyList())
                            .generateSystemEvent(Boolean.FALSE).build());

            assertTrue("Contentlet is on the wrong Workflow Step", LEGAL_APPROVAL_STEP_NAME
                    .equals(workflowAPI.findStepByContentlet(contentlet1).getName()));

            //As Publisher - publish
            actions = workflowAPI.findAvailableActions(contentlet1, chrisPublisher);
            if (actions.isEmpty() || actions.size() != 2) {
                assertTrue("Incorrect number of actions available", false);
            }
            if (!RETURN_FOR_EDITS_ACTION_NAME.equals(actions.get(0).getName())) {
                assertTrue("Wrong action available", false);
            }
            if (!PUBLISH_ACTION_NAME.equals(actions.get(1).getName())) {
                assertTrue("Wrong action available", false);
            }

            final WorkflowAction publish = actions.get(1);

            contentlet1 = APILocator.getWorkflowAPI().fireContentWorkflow(contentlet1,
                    new ContentletDependencies.Builder().respectAnonymousPermissions(Boolean.FALSE)
                            .modUser(chrisPublisher)
                            .relationships(contentletRelationships)
                            .workflowActionId(publish.getId()) //Send For Review
                            .workflowActionComments(StringPool.BLANK)
                            .workflowAssignKey(StringPool.BLANK)
                            .categories(Collections.emptyList())
                            .generateSystemEvent(Boolean.FALSE).build());

            assertTrue("Contentlet is on the wrong Workflow Step", PUBLISHED_STEP_NAME
                    .equals(workflowAPI.findStepByContentlet(contentlet1).getName()));
            assertTrue(title2.equals(contentlet1.getStringProperty(FIELD_VAR_NAME)));
            assertTrue(contentlet1.isLive());

        } finally {
            /*
		     * Clean test
		     */
            contentTypeAPI.delete(contentType4);
        }
    }

    /**
     * Validate if the scheme is present in the list of schemes
     *
     * @param scheme WorkflowScheme to check
     * @param schemes List of WorkflowSchemes to compare
     * @return true if exist, false if not
     */
    protected static boolean containsScheme(WorkflowScheme scheme, List<WorkflowScheme> schemes) {
        boolean containsScheme = false;
        for (WorkflowScheme compareScheme : schemes) {
            if (compareScheme.getId().equals(scheme.getId())) {
                containsScheme = true;
                break;
            }
        }
        return containsScheme;
    }

    /**
     * Generate the test content type
     */
    protected static ContentType insertContentType(String contentTypeName, BaseContentType baseType)
            throws DotDataException, DotSecurityException {

        ContentTypeBuilder builder = ContentTypeBuilder.builder(baseType.immutableClass())
                .description("description")
                .expireDateVar(null).folder(FolderAPI.SYSTEM_FOLDER).host(Host.SYSTEM_HOST)
                .name(contentTypeName).owner("owner")
                .variable("velocityVarName" + contentTypeName);

        ContentType type = builder.build();
        type = contentTypeAPI.save(type);

        Field savedField = ImmutableTextField.builder()
                .name(FIELD_NAME)
                .variable(FIELD_VAR_NAME)
                .required(true)
                .listed(true)
                .indexed(true)
                .sortOrder(1)
                .contentTypeId(type.id())
                .fixed(true)
                .searchable(true)
                .values("")
                .build();

        fieldAPI.save(savedField, user);

        return type;
    }

    /**
     * Create a WorkflowScheme
     *
     * @param schemeName Name of the new Scheme
     * @return the new Scheme
     */
    protected static WorkflowScheme addWorkflowScheme(final String schemeName)
            throws DotDataException, DotSecurityException {
        WorkflowScheme scheme = null;
        try {
            scheme = new WorkflowScheme();
            scheme.setName(schemeName);
            scheme.setDescription("testing workflows " + schemeName);
            scheme.setCreationDate(new Date());
            workflowAPI.saveScheme(scheme, APILocator.systemUser());
        } catch (AlreadyExistException e) {
            //scheme already exist
        }
        return scheme;

    }

    /**
     * Create a new Workflow Step
     *
     * @param name Name of the step
     * @param order step order
     * @param resolved Is resolved
     * @param enableEscalation Allows Escalations
     * @param schemeId Scheme Id
     * @return The created step
     */
    protected static WorkflowStep addWorkflowStep(final String name, final int order,
            final boolean resolved,
            final boolean enableEscalation, final String schemeId)
            throws DotDataException, DotSecurityException {
        WorkflowStep step = null;
        try {
            step = new WorkflowStep();
            step.setCreationDate(new Date());
            step.setEnableEscalation(enableEscalation);
            step.setMyOrder(order);
            step.setName(name);
            step.setResolved(resolved);
            step.setSchemeId(schemeId);
            workflowAPI.saveStep(step, user);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }
        return step;
    }

    /**
     * Add a workflowAction
     *
     * @param name Name of the action
     * @param order Order
     * @param nextStep next step
     * @param requiresCheckout is checkout required
     * @param stepId Current step id
     * @param whoCanUse Role permissions
     * @return A workflowAction
     */
    protected static WorkflowAction addWorkflowAction(final String name, final int order,
            final String nextStep,
            final boolean requiresCheckout, final String stepId, final Role whoCanUse,
            final String schemeId)
            throws DotDataException, DotSecurityException {

        WorkflowAction action = null;
        try {
            action = new WorkflowAction();
            action.setName(name);
            action.setSchemeId(schemeId);
            action.setOwner(whoCanUse.getId());
            action.setOrder(order);
            action.setNextStep(nextStep);
            action.setRequiresCheckout(false);
            action.setStepId(stepId);
            action.setNextAssign(whoCanUse.getId());
            action.setCommentable(true);
            action.setAssignable(false);
            action.setShowOn(WorkflowState.LOCKED, WorkflowState.UNLOCKED, WorkflowState.NEW,
                    WorkflowState.PUBLISHED, WorkflowState.UNPUBLISHED, WorkflowState.ARCHIVED);

            workflowAPI.saveAction(action,
                    Arrays.asList(new Permission[]{
                            new Permission(action.getId(),
                                    whoCanUse.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(action.getId(), stepId, APILocator.systemUser());
        } catch (AlreadyExistException e) {
            //scheme already exist
        }
        return action;
    }

    /**
     * Add a subaction
     *
     * @param name name of the subaction
     * @param actionId Action id where the subaction should be associated
     * @param actionClassToUse The subaction classs to use
     * @param order Order of executions
     */
    protected static WorkflowActionClass addSubActionClass(final String name, final String actionId,
            final Class actionClassToUse, final int order)
            throws DotDataException, DotSecurityException {
        WorkflowActionClass actionClass = null;
        try {
            actionClass = new WorkflowActionClass();
            actionClass.setActionId(actionId);
            actionClass.setClazz(actionClassToUse.getCanonicalName());
            actionClass.setName(name);
            actionClass.setOrder(order);
            workflowAPI.saveActionClass(actionClass, APILocator.systemUser());
        } catch (AlreadyExistException e) {
            //scheme already exist
        }
        return actionClass;
    }

    /**
     * Remove the content type and workflows created
     */
    @AfterClass
    public static void cleanup() throws DotDataException, DotSecurityException {

        contentTypeAPI.delete(contentType);
        contentTypeAPI.delete(contentType2);
        contentTypeAPI.delete(contentType3);
        try {
            //Deleting workflow 1
            workflowAPI.deleteAction(workflowScheme1Step1Action1, user);
            workflowAPI.deleteAction(workflowScheme1Step2Action1, user);

            workflowAPI.deleteStep(workflowScheme1Step1, user);
            workflowAPI.deleteStep(workflowScheme1Step2, user);

            workflowScheme1.setArchived(true);
            workflowAPI.saveScheme(workflowScheme1, user);
            workflowAPI.deleteScheme(workflowScheme1, user);

            //Deleting workflow 2
            workflowAPI.deleteAction(workflowScheme2Step1Action1, user);
            workflowAPI.deleteAction(workflowScheme2Step2Action1, user);
            workflowAPI.deleteStep(workflowScheme2Step1, user);
            workflowAPI.deleteStep(workflowScheme2Step2, user);

            workflowScheme2.setArchived(true);
            workflowAPI.saveScheme(workflowScheme2, user);
            workflowAPI.deleteScheme(workflowScheme2, user);

            //Deleting workflow 3
            workflowAPI.deleteAction(workflowScheme3Step1Action1, user);
            workflowAPI.deleteAction(workflowScheme3Step2Action1, user);
            workflowAPI.deleteAction(workflowScheme3Step2Action2, user);

            workflowAPI.deleteStep(workflowScheme3Step1, user);
            workflowAPI.deleteStep(workflowScheme3Step2, user);

            workflowScheme3.setArchived(true);
            workflowAPI.saveScheme(workflowScheme3, user);
            workflowAPI.deleteScheme(workflowScheme3, user);

            //Deleting workflow 4
            workflowAPI.deleteAction(workflowScheme4Step1ActionContributor, user);
            workflowAPI.deleteAction(workflowScheme4Step1ActionEdit, user);
            workflowAPI.deleteAction(workflowScheme4Step1ActionEditPermissions, user);
            workflowAPI.deleteAction(workflowScheme4Step1ActionPublish, user);
            workflowAPI.deleteAction(workflowScheme4Step1ActionView, user);

            workflowAPI.deleteAction(workflowScheme4Step2ActionReviewer, user);
            workflowAPI.deleteAction(workflowScheme4Step2ActionEdit, user);
            workflowAPI.deleteAction(workflowScheme4Step2ActionEditPermissions, user);
            workflowAPI.deleteAction(workflowScheme4Step2ActionPublish, user);
            workflowAPI.deleteAction(workflowScheme4Step2ActionView, user);

            workflowAPI.deleteAction(workflowScheme4Step3ActionPublisher, user);
            workflowAPI.deleteAction(workflowScheme4Step3ActionEdit, user);
            workflowAPI.deleteAction(workflowScheme4Step3ActionEditPermissions, user);
            workflowAPI.deleteAction(workflowScheme4Step3ActionPublish, user);
            workflowAPI.deleteAction(workflowScheme4Step3ActionView, user);

            workflowAPI.deleteStep(workflowScheme4Step1, user);
            workflowAPI.deleteStep(workflowScheme4Step2, user);
            workflowAPI.deleteStep(workflowScheme4Step3, user);

            workflowScheme4.setArchived(true);
            workflowAPI.saveScheme(workflowScheme4, user);
            workflowAPI.deleteScheme(workflowScheme4, user);

            //Deleting workflow 5
            workflowAPI.deleteAction(workflowScheme5Step1Action1, user);
            workflowAPI.deleteStep(workflowScheme5Step1, user);

            workflowScheme5.setArchived(true);
            workflowAPI.saveScheme(workflowScheme5, user);
            workflowAPI.deleteScheme(workflowScheme5, user);

        }catch (AlreadyExistException e){

        }


    }

    /**
     * This method generate a replica of the current Document Management Workflow
     *
     * @param newWorkflowScheme The new for the workflow replica
     * @return The new workflow
     */
    public static WorkflowScheme createDocumentManagentReplica(String newWorkflowScheme)
            throws DotDataException, DotSecurityException {

        final WorkflowScheme scheme = addWorkflowScheme(newWorkflowScheme);
        final Role adminRole = roleAPI.loadCMSAdminRole();
        final Role anonymus = roleAPI.loadCMSAnonymousRole();
        //Create Steps

        //Editing Step
        WorkflowStep editingStep = addWorkflowStep(EDITING_STEP_NAME, 0, Boolean.FALSE,
                Boolean.FALSE, scheme.getId());

        //Review Step
        WorkflowStep reviewStep = addWorkflowStep(REVIEW_STEP_NAME, 1, Boolean.FALSE, Boolean.FALSE,
                scheme.getId());

        //Legal Approval Step
        WorkflowStep legalApprovalStep = addWorkflowStep(LEGAL_APPROVAL_STEP_NAME, 2, Boolean.FALSE,
                Boolean.FALSE, scheme.getId());

        //Published Step
        WorkflowStep publishedStep = addWorkflowStep(PUBLISHED_STEP_NAME, 3, Boolean.TRUE,
                Boolean.FALSE, scheme.getId());

        //Archived Step
        WorkflowStep archivedStep = addWorkflowStep(ARCHIVED_STEP_NAME, 4, Boolean.TRUE,
                Boolean.FALSE, scheme.getId());

        //Create Actions

        //Save as Draft Action
        WorkflowAction saveAsDraftAction = null;
        try {
            saveAsDraftAction = new WorkflowAction();
            saveAsDraftAction.setName(SAVE_AS_DRAFT_ACTION_NAME);
            saveAsDraftAction.setSchemeId(scheme.getId());
            saveAsDraftAction.setOwner(adminRole.getId());
            saveAsDraftAction.setOrder(0);
            saveAsDraftAction.setNextStep(WorkflowAction.CURRENT_STEP);
            saveAsDraftAction.setNextAssign(anonymus.getId());
            saveAsDraftAction.setCommentable(true);
            saveAsDraftAction.setAssignable(false);
            saveAsDraftAction.setShowOn(WorkflowState.LOCKED, WorkflowState.NEW,
                    WorkflowState.PUBLISHED, WorkflowState.UNPUBLISHED);

            workflowAPI.saveAction(saveAsDraftAction,
                    Arrays.asList(new Permission[]{
                            new Permission(saveAsDraftAction.getId(),
                                    anyWhoEdit.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(saveAsDraftAction.getId(), editingStep.getId(),
                    APILocator.systemUser(), 0);
            workflowAPI.saveAction(saveAsDraftAction.getId(), reviewStep.getId(),
                    APILocator.systemUser(), 0);
            workflowAPI.saveAction(saveAsDraftAction.getId(), legalApprovalStep.getId(),
                    APILocator.systemUser(), 0);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Send for review Action
        WorkflowAction sendForReviewAction = null;
        try {
            sendForReviewAction = new WorkflowAction();
            sendForReviewAction.setName(SEND_FOR_REVIEW_ACTION_NAME);
            sendForReviewAction.setSchemeId(scheme.getId());
            sendForReviewAction.setOwner(adminRole.getId());
            sendForReviewAction.setOrder(1);
            sendForReviewAction.setNextStep(reviewStep.getId());
            sendForReviewAction.setNextAssign(reviewer.getId());
            sendForReviewAction.setCommentable(true);
            sendForReviewAction.setAssignable(false);
            sendForReviewAction.setShowOn(WorkflowState.LOCKED, WorkflowState.UNLOCKED,
                    WorkflowState.PUBLISHED, WorkflowState.UNPUBLISHED);

            workflowAPI.saveAction(sendForReviewAction,
                    Arrays.asList(new Permission[]{
                            new Permission(sendForReviewAction.getId(),
                                    anyWhoEdit.getId(),
                                    PermissionAPI.PERMISSION_USE),
                            new Permission(sendForReviewAction.getId(),
                                    contributor.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(sendForReviewAction.getId(), editingStep.getId(),
                    APILocator.systemUser(), 1);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Send to Legal Action
        WorkflowAction sendToLegalAction = null;
        try {
            sendToLegalAction = new WorkflowAction();
            sendToLegalAction.setName(SEND_TO_LEGAL_ACTION_NAME);
            sendToLegalAction.setSchemeId(scheme.getId());
            sendToLegalAction.setOwner(adminRole.getId());
            sendToLegalAction.setOrder(1);
            sendToLegalAction.setNextStep(legalApprovalStep.getId());
            sendToLegalAction.setNextAssign(publisher.getId());
            sendToLegalAction.setCommentable(true);
            sendToLegalAction.setAssignable(false);
            sendToLegalAction.setShowOn(WorkflowState.LOCKED, WorkflowState.UNLOCKED,
                    WorkflowState.PUBLISHED, WorkflowState.UNPUBLISHED);

            workflowAPI.saveAction(sendToLegalAction,
                    Arrays.asList(new Permission[]{
                            new Permission(sendToLegalAction.getId(),
                                    reviewer.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(sendToLegalAction.getId(), editingStep.getId(),
                    APILocator.systemUser(), 1);
            workflowAPI.saveAction(sendToLegalAction.getId(), reviewStep.getId(),
                    APILocator.systemUser(), 2);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Publish Action
        WorkflowAction publishAction = null;
        try {
            publishAction = new WorkflowAction();
            publishAction.setName(PUBLISH_ACTION_NAME);
            publishAction.setSchemeId(scheme.getId());
            publishAction.setOwner(adminRole.getId());
            publishAction.setOrder(1);
            publishAction.setNextStep(publishedStep.getId());
            publishAction.setNextAssign(anonymus.getId());
            publishAction.setCommentable(false);
            publishAction.setAssignable(false);
            publishAction.setShowOn(WorkflowState.LOCKED, WorkflowState.UNLOCKED,
                    WorkflowState.UNPUBLISHED);

            workflowAPI.saveAction(publishAction,
                    Arrays.asList(new Permission[]{
                            new Permission(publishAction.getId(),
                                    anyWhoPublish.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI
                    .saveAction(publishAction.getId(), editingStep.getId(), APILocator.systemUser(),
                            3);
            workflowAPI
                    .saveAction(publishAction.getId(), reviewStep.getId(), APILocator.systemUser(),
                            3);
            workflowAPI.saveAction(publishAction.getId(), legalApprovalStep.getId(),
                    APILocator.systemUser(), 3);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Return For Edit Action
        WorkflowAction returnForEditAction = null;
        try {
            returnForEditAction = new WorkflowAction();
            returnForEditAction.setName(RETURN_FOR_EDITS_ACTION_NAME);
            returnForEditAction.setSchemeId(scheme.getId());
            returnForEditAction.setOwner(adminRole.getId());
            returnForEditAction.setOrder(1);
            returnForEditAction.setNextStep(editingStep.getId());
            returnForEditAction.setNextAssign(contributor.getId());
            returnForEditAction.setCommentable(true);
            returnForEditAction.setAssignable(true);
            returnForEditAction.setShowOn(WorkflowState.LOCKED, WorkflowState.UNLOCKED,
                    WorkflowState.PUBLISHED, WorkflowState.UNPUBLISHED);

            workflowAPI.saveAction(returnForEditAction,
                    Arrays.asList(new Permission[]{
                            new Permission(returnForEditAction.getId(),
                                    reviewer.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(returnForEditAction.getId(), reviewStep.getId(),
                    APILocator.systemUser(), 1);
            workflowAPI.saveAction(returnForEditAction.getId(), legalApprovalStep.getId(),
                    APILocator.systemUser(), 1);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Republish Action
        WorkflowAction republishAction = null;
        try {
            republishAction = new WorkflowAction();
            republishAction.setName(REPUBLISH_ACTION_NAME);
            republishAction.setSchemeId(scheme.getId());
            republishAction.setOwner(adminRole.getId());
            republishAction.setOrder(0);
            republishAction.setNextStep(publishedStep.getId());
            republishAction.setNextAssign(anonymus.getId());
            republishAction.setCommentable(false);
            republishAction.setAssignable(false);
            republishAction.setShowOn(WorkflowState.LOCKED, WorkflowState.UNLOCKED,
                    WorkflowState.PUBLISHED, WorkflowState.UNPUBLISHED);

            workflowAPI.saveAction(republishAction,
                    Arrays.asList(new Permission[]{
                            new Permission(republishAction.getId(),
                                    anonymus.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(republishAction.getId(), publishedStep.getId(),
                    APILocator.systemUser(), 0);
            workflowAPI.saveAction(republishAction.getId(), archivedStep.getId(),
                    APILocator.systemUser(), 2);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Unpublish Action
        WorkflowAction unpublishAction = null;
        try {
            unpublishAction = new WorkflowAction();
            unpublishAction.setName(UNPUBLISH_ACTION_NAME);
            unpublishAction.setSchemeId(scheme.getId());
            unpublishAction.setOwner(adminRole.getId());
            unpublishAction.setOrder(1);
            unpublishAction.setNextStep(reviewStep.getId());
            unpublishAction.setNextAssign(anonymus.getId());
            unpublishAction.setCommentable(false);
            unpublishAction.setAssignable(false);
            unpublishAction.setShowOn(WorkflowState.LOCKED, WorkflowState.UNLOCKED,
                    WorkflowState.PUBLISHED);

            workflowAPI.saveAction(unpublishAction,
                    Arrays.asList(new Permission[]{
                            new Permission(unpublishAction.getId(),
                                    anyWhoPublish.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(unpublishAction.getId(), publishedStep.getId(),
                    APILocator.systemUser(), 1);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Archive action
        WorkflowAction archiveAction = null;
        try {
            archiveAction = new WorkflowAction();
            archiveAction.setName(ARCHIVE_ACTION_NAME);
            archiveAction.setSchemeId(scheme.getId());
            archiveAction.setOwner(adminRole.getId());
            archiveAction.setOrder(1);
            archiveAction.setNextStep(archivedStep.getId());
            archiveAction.setNextAssign(anonymus.getId());
            archiveAction.setCommentable(false);
            archiveAction.setAssignable(false);
            archiveAction.setShowOn(WorkflowState.UNLOCKED, WorkflowState.UNPUBLISHED);

            workflowAPI.saveAction(archiveAction,
                    Arrays.asList(new Permission[]{
                            new Permission(archiveAction.getId(),
                                    publisher.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(archiveAction.getId(), publishedStep.getId(),
                    APILocator.systemUser(), 2);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Delete action
        WorkflowAction deleteAction = null;
        try {
            deleteAction = new WorkflowAction();
            deleteAction.setName(DELETE_ACTION_NAME);
            deleteAction.setSchemeId(scheme.getId());
            deleteAction.setOwner(adminRole.getId());
            deleteAction.setOrder(1);
            deleteAction.setNextStep(archivedStep.getId());
            deleteAction.setNextAssign(anonymus.getId());
            deleteAction.setCommentable(false);
            deleteAction.setAssignable(false);
            deleteAction.setShowOn(WorkflowState.UNLOCKED, WorkflowState.ARCHIVED);

            workflowAPI.saveAction(deleteAction,
                    Arrays.asList(new Permission[]{
                            new Permission(archiveAction.getId(),
                                    publisher.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI
                    .saveAction(deleteAction.getId(), archivedStep.getId(), APILocator.systemUser(),
                            1);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //reset workflow action
        WorkflowAction resetWorkflowAction = null;
        try {
            resetWorkflowAction = new WorkflowAction();
            resetWorkflowAction.setName(RESET_WORKFLOW_ACTION_NAME);
            resetWorkflowAction.setSchemeId(scheme.getId());
            resetWorkflowAction.setOwner(adminRole.getId());
            resetWorkflowAction.setOrder(1);
            resetWorkflowAction.setNextStep(archivedStep.getId());
            resetWorkflowAction.setNextAssign(anonymus.getId());
            resetWorkflowAction.setCommentable(false);
            resetWorkflowAction.setAssignable(false);
            resetWorkflowAction.setShowOn(WorkflowState.LOCKED, WorkflowState.UNLOCKED,
                    WorkflowState.ARCHIVED);

            workflowAPI.saveAction(resetWorkflowAction,
                    Arrays.asList(new Permission[]{
                            new Permission(archiveAction.getId(),
                                    publisher.getId(),
                                    PermissionAPI.PERMISSION_USE)}),
                    APILocator.systemUser());

            workflowAPI.saveAction(resetWorkflowAction.getId(), archivedStep.getId(),
                    APILocator.systemUser(), 0);
        } catch (AlreadyExistException e) {
            //scheme already exist
        }

        //Add subactions to the actions

        //Save as Draft subactionss
        addSubActionClass(SAVE_AS_DRAFT_SUBACTION, saveAsDraftAction.getId(),
                SaveContentAsDraftActionlet.class, 0);

        //Send For Review subactions
        addSubActionClass(SAVE_AS_DRAFT_SUBACTION, sendForReviewAction.getId(),
                SaveContentAsDraftActionlet.class, 0);
        addSubActionClass(UNLOCK_SUBACTION, sendForReviewAction.getId(),
                CheckinContentActionlet.class, 1);

        //Send to Legal subactions
        addSubActionClass(SAVE_AS_DRAFT_SUBACTION, sendToLegalAction.getId(),
                SaveContentAsDraftActionlet.class, 0);
        addSubActionClass(UNLOCK_SUBACTION, sendToLegalAction.getId(),
                CheckinContentActionlet.class, 1);

        //Publish subactions
        addSubActionClass(SAVE_CONTENT_SUBACTION, publishAction.getId(), SaveContentActionlet.class,
                0);
        addSubActionClass(PUBLISH_SUBACTION, publishAction.getId(), PublishContentActionlet.class,
                1);
        addSubActionClass(UNLOCK_SUBACTION, publishAction.getId(), CheckinContentActionlet.class,
                2);

        //Return for Edit subactions
        addSubActionClass(SAVE_AS_DRAFT_SUBACTION, returnForEditAction.getId(),
                SaveContentAsDraftActionlet.class, 0);
        addSubActionClass(UNLOCK_SUBACTION, returnForEditAction.getId(),
                CheckinContentActionlet.class, 1);

        //Republish subactions
        addSubActionClass(SAVE_AS_DRAFT_SUBACTION, republishAction.getId(),
                SaveContentAsDraftActionlet.class, 0);
        addSubActionClass(UNARCHIVE_SUBACTION, republishAction.getId(),
                UnarchiveContentActionlet.class, 1);
        addSubActionClass(PUBLISH_SUBACTION, republishAction.getId(), PublishContentActionlet.class,
                2);
        addSubActionClass(UNLOCK_SUBACTION, republishAction.getId(), CheckinContentActionlet.class,
                3);

        //Unpublish subactions
        addSubActionClass(SAVE_AS_DRAFT_SUBACTION, unpublishAction.getId(),
                SaveContentAsDraftActionlet.class, 0);
        addSubActionClass(UNPUBLISH_SUBACTION, unpublishAction.getId(),
                UnpublishContentActionlet.class, 1);
        addSubActionClass(UNLOCK_SUBACTION, unpublishAction.getId(), CheckinContentActionlet.class,
                2);

        //Archive subactions
        addSubActionClass(UNLOCK_SUBACTION, archiveAction.getId(), CheckinContentActionlet.class,
                0);
        addSubActionClass(UNPUBLISH_SUBACTION, archiveAction.getId(),
                UnpublishContentActionlet.class, 1);
        addSubActionClass(ARCHIVE_SUBACTION, archiveAction.getId(), ArchiveContentActionlet.class,
                2);

        //Reset workflow subactions
        addSubActionClass(UNLOCK_SUBACTION, resetWorkflowAction.getId(),
                CheckinContentActionlet.class, 0);
        addSubActionClass(UNARCHIVE_SUBACTION, resetWorkflowAction.getId(),
                UnpublishContentActionlet.class, 1);
        addSubActionClass(RESET_WORKFLOW_SUBACTION, resetWorkflowAction.getId(),
                ResetTaskActionlet.class, 2);

        //Delete subactions
        addSubActionClass(UNLOCK_SUBACTION, deleteAction.getId(), CheckinContentActionlet.class, 0);
        addSubActionClass(ARCHIVE_SUBACTION, deleteAction.getId(), ArchiveContentActionlet.class,
                1);
        addSubActionClass(RESET_WORKFLOW_SUBACTION, deleteAction.getId(), ResetTaskActionlet.class,
                2);
        addSubActionClass(DELETE_SUBACTION, deleteAction.getId(), DeleteContentActionlet.class, 3);

        return scheme;
    }

}