import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Quizz } from '../../models/quizz.model';
import { Question } from '../../models/question.model';
import { QuizzService } from '../../services/quizz.service';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { Me } from '../../models/me.model';

@Component({
  selector: 'quizz-info',
  templateUrl: './quizz-info.component.html',
  styleUrls: ['../../styles/quiz.css']
})
export class QuizzInfoComponent {
  me: Me;
  admin: boolean;
  logged: boolean;
  id: number;
  quizz: Quizz;
  score:number=0;

  constructor(private activatedRoute: ActivatedRoute, private router: Router, private service: QuizzService, private userService: UserService) {
    activatedRoute.params.subscribe(params => {
      this.id = params['id'];
    });
    service.getQuizz(this.id).subscribe(
      response => {
        this.quizz = response as Quizz;
      },
      error => console.error(error)
    );

    userService.me().subscribe(
      response => {
        this.me = response as Me
        this.admin = (this.me.mail == "xd");
        this.logged = true;
      },
      error => {
        this.admin = false;
        this.logged = false;
        this.router.navigate(['/login']);
      }
    );
  }

  submit(): void {
    this.score = 0;
    if (this.quizz?.questions?.length) {
      for (let question of this.quizz.questions) {
        if (question.selected != null){
            question.select1 = (question.selected === question.option1);
            question.select2 = (question.selected === question.option2);
            question.select3 = (question.selected === question.option3);
            question.select4 = (question.selected === question.option4);
          if (question.selected === question.answer) {
            this.score++;
          }
        }
      }
      this.quizz.score=this.score
        this.service.submitQuizz(this.quizz.id, this.quizz).subscribe(
        response => {
          const navigationExtras: NavigationExtras = { state: { quizz: this.quizz } };
          this.router.navigate(['result'], { relativeTo: this.activatedRoute, ...navigationExtras });
        },
        error => {
          console.error("Error al enviar el quizz:", error);
          alert("No se pudo enviar el quizz.");
        }
      );
    }
  }

  deleteQuizz(id: number | undefined) {
    this.service.deleteQuizz(id).subscribe(
      _ => this.router.navigate(['/quizzes']),
      error => console.error("Error deleting the quizz: " + error)
    );
  }
}