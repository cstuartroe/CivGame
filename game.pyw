import tkinter as tk
import random
import time

back = '#eeeebb'

class Application(tk.Frame):
    def __init__(self, master=None):
        tk.Frame.__init__(self, master,bg=back,cursor='target')
        self.pack()
        self.create_widgets()
        
    def create_widgets(self):        
        self.paintboard = tk.Canvas(self, width=800, height=480)
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
        
        self.castle = tk.PhotoImage(file='images/castle.gif')
        self.spruces = tk.PhotoImage(file='images/spruces.gif')
        self.gravel = tk.PhotoImage(file='images/gravel.gif')
        
        for i in range(25):
            for j in range(15):
                self.backdrop = self.paintboard.create_rectangle(32*i,32*j,32*(i+1),32*(j+1),
                                                                 fill='#aaddbb', outline='')
                self.paintboard.create_image(32*i,32*j,anchor='nw',image=self.gravel)
                if random.random() < .02:
                    self.paintboard.create_image(32*i,32*j,anchor='nw',image=self.castle)
                elif random.random() < .3:
                    self.paintboard.create_image(32*i,32*j,anchor='nw',image=self.spruces)

root = tk.Tk()
root.resizable(False,False)
app = Application(master=root)
app.master.title('This is a game.')
app.mainloop()
