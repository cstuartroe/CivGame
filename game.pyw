import tkinter as tk
import random
import time
import threading

root = tk.Tk()
root.resizable(False,False)

back = '#eeeebb'

blue_castle = tk.PhotoImage(file='images/blue_castle.gif')
red_castle = tk.PhotoImage(file='images/red_castle.gif')
green_castle = tk.PhotoImage(file='images/green_castle.gif')
yellow_castle = tk.PhotoImage(file='images/yellow_castle.gif')
orange_castle = tk.PhotoImage(file='images/orange_castle.gif')
purple_castle = tk.PhotoImage(file='images/purple_castle.gif')
navy_castle = tk.PhotoImage(file='images/navy_castle.gif')
tricolor_castle = tk.PhotoImage(file='images/tricolor_castle.gif')
symbol_castle = tk.PhotoImage(file='images/symbol_castle.gif')
chalice_castle = tk.PhotoImage(file='images/chalice_castle.gif')
surrendered_castle = tk.PhotoImage(file='images/surrendered_castle.gif')

spruces = tk.PhotoImage(file='images/spruces.gif')
oaks = tk.PhotoImage(file='images/oaks.gif')
gravel = tk.PhotoImage(file='images/gravel.gif')
overlays = {'blue castle':blue_castle,'red castle':red_castle,'green castle':green_castle,
            'yellow castle':yellow_castle,'orange castle':orange_castle,
            'purple castle':purple_castle,'navy castle':navy_castle,
            'tricolor castle':tricolor_castle,'symbol castle':symbol_castle,
            'chalice castle':chalice_castle,'surrendered castle':surrendered_castle,
            'spruces':spruces,'oaks':oaks,'none':None}

def toHexStr(n):
    return ("0x%x" % n)[2:].rjust(2,'0')

class SquareLand(tk.Canvas):
    def __init__(self, master=None,humidity=0,temperature=0):
        tk.Canvas.__init__(self, master, width=32, height=32, highlightthickness=0)
        self.bind("<Button-1>", self.callback)
        self.is_focused = False
        
        seed = random.random()
        if seed < .02:
            self.overlay = list(overlays)[int(seed/.002)]
        elif seed < .16:
            self.overlay = 'spruces'
        elif seed < .3:
            self.overlay = 'oaks'
        else:
            self.overlay = 'none'
        self.humidity = random.randrange(100)
        self.temperature = random.randrange(100)
##        self.bg = '#aaddbb'
        self.bg = self.find_bg()
        self.redraw()

    def redraw(self):
        try:
            self.create_rectangle(0,0,32,32,fill=self.bg,outline='')
        except Exception:
            print(self.temperature,self.humidity)
        if self.is_focused:
            self.create_rectangle(2,2,30,30,fill='',outline='red')
        self.create_image(0,0,anchor='nw',image=gravel)
        self.create_image(0,0,anchor='nw',image=overlays[self.overlay])

    def callback(self,event):
        event.widget.focus()

    def focus(self):
        self.delete('all')
        mapboard = self.master
        
        mapboard.focused.is_focused = False
        mapboard.focused.redraw()
        
        mapboard.focused = self
        self.is_focused = True
        self.redraw()

        mapboard.master.a['text'] = self.bg
        mapboard.master.c['text'] = 'temperature ' + str(self.real_temperature())
        mapboard.master.d['text'] = 'humidity ' + str(self.real_humidity())

    def find_bg(self):
        red = 255 - int(.85*self.temperature) - int(1.7*self.humidity)
        grn = 255 - int(.85*self.temperature) - int(.85*self.humidity)
        blu = 255 - int(2.55*self.temperature) - int(1.5*self.humidity)
        blu = blu if blu>=0 else 0
        return '#' + toHexStr(red) + toHexStr(grn) + toHexStr(blu)

    def real_temperature(self):
        return str(int(.4*self.temperature - 5)) + 'C'

    def real_humidity(self):
        return str(self.humidity) + '%'

class MapBoard(tk.Frame):
    def __init__(self, master, width=800, height=480):
        tk.Frame.__init__(self, master, width=width, height=height)

        self.squares = ['']*40
        for i in range(20):
##            t = threading.Thread(target=self.add_row, args=(i,))
##            t.start()
            self.add_row(i)
        self.focused = self.squares[0][0]

    def add_row(self,index):
        row = []
        for j in range(20):
            sl = SquareLand(self)
            sl.grid(row = index, column = j)
            row += [sl]
        self.squares[index] = row
        

class Application(tk.Frame):
    def __init__(self, master=None):
        tk.Frame.__init__(self, master,bg=back,cursor='target')
        self.pack()
        self.create_widgets()
        
    def create_widgets(self):     
        self.paintboard = MapBoard(self)
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

app = Application(master=root)
app.master.title('This is a game.')
app.mainloop()
