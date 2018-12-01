import { PhonenumberPipe } from './phonenumber.pipe';

describe('PhonenumberPipe', () => {
  it('create an instance', () => {
    const pipe = new PhonenumberPipe();
    expect(pipe).toBeTruthy();
  });
});
