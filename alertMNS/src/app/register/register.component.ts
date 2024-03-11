import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {AuthService} from "../_services/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit, OnDestroy {

    errorMessage! : string;
    AuthUserSub! : Subscription;

    constructor(private authService : AuthService, private router: Router) {
    }

    ngOnInit() {
        this.AuthUserSub = this.authService.AuthenticatedUser$.subscribe({
            next : user => {
                if(user) {
                    this.router.navigate(['home']);
                }
            }
        })
    }

    onSubmitRegister(formRegister: NgForm) {
        if(!formRegister.valid){
            return;
        }
        const firstname = formRegister.value.firstname;
        const lastname = formRegister.value.lastname;
        const email = formRegister.value.email;
        const password = formRegister.value.password;

        this.authService.register(firstname, lastname, email, password).subscribe({
            next : userData => {
                this.router.navigate(['home']);
            },
            error : err => {
                this.errorMessage = err;
                console.log(err);
            }
        })
    }

    ngOnDestroy() {
        this.AuthUserSub.unsubscribe();
    }

    protected readonly console = console;
}
