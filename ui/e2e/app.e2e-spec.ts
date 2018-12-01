import { SchedulesysUiPage } from './app.po';

describe('schedulesys-ui App', () => {
  let page: SchedulesysUiPage;

  beforeEach(() => {
    page = new SchedulesysUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
