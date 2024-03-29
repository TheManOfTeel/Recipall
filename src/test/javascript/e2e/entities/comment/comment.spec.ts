import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  CommentComponentsPage,
  /* CommentDeleteDialog,
   */ CommentUpdatePage
} from './comment.page-object';

const expect = chai.expect;

describe('Comment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let commentComponentsPage: CommentComponentsPage;
  let commentUpdatePage: CommentUpdatePage;
  /* let commentDeleteDialog: CommentDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Comments', async () => {
    await navBarPage.goToEntity('comment');
    commentComponentsPage = new CommentComponentsPage();
    await browser.wait(ec.visibilityOf(commentComponentsPage.title), 5000);
    expect(await commentComponentsPage.getTitle()).to.eq('Comments');
  });

  it('should load create Comment page', async () => {
    await commentComponentsPage.clickOnCreateButton();
    commentUpdatePage = new CommentUpdatePage();
    expect(await commentUpdatePage.getPageTitle()).to.eq('Create or edit a Comment');
    await commentUpdatePage.cancel();
  });

  /*  it('should create and save Comments', async () => {
        const nbButtonsBeforeCreate = await commentComponentsPage.countDeleteButtons();

        await commentComponentsPage.clickOnCreateButton();
        await promise.all([
            commentUpdatePage.setCommentInfoInput('commentInfo'),
            commentUpdatePage.setDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            commentUpdatePage.setScoreInput('5'),
            commentUpdatePage.userSelectLastOption(),
            commentUpdatePage.recipeSelectLastOption(),
        ]);
        expect(await commentUpdatePage.getCommentInfoInput()).to.eq('commentInfo', 'Expected CommentInfo value to be equals to commentInfo');
        expect(await commentUpdatePage.getDateInput()).to.contain('2001-01-01T02:30', 'Expected date value to be equals to 2000-12-31');
        expect(await commentUpdatePage.getScoreInput()).to.eq('5', 'Expected score value to be equals to 5');
        await commentUpdatePage.save();
        expect(await commentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await commentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /*  it('should delete last Comment', async () => {
        const nbButtonsBeforeDelete = await commentComponentsPage.countDeleteButtons();
        await commentComponentsPage.clickOnLastDeleteButton();

        commentDeleteDialog = new CommentDeleteDialog();
        expect(await commentDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Comment?');
        await commentDeleteDialog.clickOnConfirmButton();

        expect(await commentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
