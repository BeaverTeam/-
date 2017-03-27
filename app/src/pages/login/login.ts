import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { HomePage } from '../home/home';
import { Auth } from '../../providers/Auth/auth.service';
import { Validator } from '../../providers/Auth/Validator';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {
  pageName: string = 'signIn';
  errorMessage: string = '';
  validator: Validator = new Validator();

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public auth: Auth) {}

  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }

  // 登录校验
  signInValidator(formData, inputId) {
    this.errorMessage = this.validator.signInValidator(formData, inputId);
  }

  signInCheck(formData) {
    // 进行前端校验
    this.signInValidator(formData, -1);
    if (this.errorMessage != '') return;

    // 发往后端进行校验
    if (this.auth.signIn(formData.signInUsername, formData.signInPassword)) {
      this.navCtrl.push(HomePage);
    } else {
      this.auth.signOut();
    }
  }

  // 注册校验
  signUpValidator(formData, inputId) {
    this.errorMessage = this.validator.signUpValidator(formData, inputId);
  }

  signUpCheck(formData) {
    // 进行前端校验
    this.signUpValidator(formData, -1);
    if (this.errorMessage != '') return;

    // TODO 发往后端进行校验
  }

  gotoRegister() {
    this.pageName = "register";
  }

  gotoLogin() {
    this.pageName = "signIn";
  }

}
