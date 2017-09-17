import tkinter as tk
import random
import time

root = tk.Tk()
root.resizable(False,False)

back = '#eeeebb'    
blue_castle = tk.PhotoImage(file='images/blue_castle.gif')
red_castle = tk.PhotoImage(file='images/red_castle.gif')
green_castle = tk.PhotoImage(file='images/green_castle.gif')
surrendered_castle = tk.PhotoImage(file='images/surrendered_castle.gif')
spruces = tk.PhotoImage(file='images/spruces.gif')
oaks = tk.PhotoImage(file='images/oaks.gif')
gravel = tk.PhotoImage(file='images/gravel.gif')

class SquareLand(tk.Canvas):
    def __init__(self, master=None):
        tk.Canvas.__init__(self, master, width=32, height=32, bg='#aaddbb', highlightthickness=0)
        self.create_image(0,0,anchor='nw',image=gravel)
        self.seed = random.random()
        if self.seed < .005:
            self.create_image(0,0,anchor='nw',image=blue_castle)
        elif self.seed < .01:
            self.create_image(0,0,anchor='nw',image=red_castle)
        elif self.seed < .015:
            self.create_image(0,0,anchor='nw',image=green_castle)
        elif self.seed < .02:
            self.create_image(0,0,anchor='nw',image=surrendered_castle)
        elif self.seed < .16:
            self.create_image(0,0,anchor='nw',image=spruces)
        elif self.seed < .3:
            self.create_image(0,0,anchor='nw',image=oaks)

class Application(tk.Frame):
    def __init__(self, master=None):
        tk.Frame.__init__(self, master,bg=back,cursor='target')
        self.pack()
        self.create_widgets()
        
    def create_widgets(self):        
        self.paintboard = tk.Frame(self, width=800, height=480)
        self.paintboard.pack(side='top')

        self.textframe = tk.Frame(self, width=800, height=160,pady=40, bg=back)
        self.textframe.pack(side = 'bottom')
        self.a = tk.Label(self.textframe, text='A is for Apple',
                          font=('Georgia',12), bg=back)
        self.a.grid(row=0,column=0)
        self.b = tk.Label(self.textframe, text='B is for Banana',
                          font=('Georgia',12), bg=back)
        self.b.grid(row=0,column=1)
        self.c = tk.Label(self.textframe, text='C is for Coconut',
                          font=('Georgia',12), bg=back)
        self.c.grid(row=1,column=0)
        self.d = tk.Label(self.textframe, text='D is for Durian',
                          font=('Georgia',12), bg=back)
        self.d.grid(row=1,column=1)
        
        for i in range(25):
            for j in range(15):
                sl = SquareLand(self.paintboard)
                sl.grid(row = j, column = i)

app = Application(master=root)
app.master.title('This is a game.')
app.mainloop()
