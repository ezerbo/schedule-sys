export class JWT {

  token: string;

  constructor(json?: any) {
    if (json != null) {
      this.token = json.id_token;
    }
  }
}
